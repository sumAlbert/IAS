import net.sf.json.JSONObject;
import question.Question;
import sql.BaseConnection;
import sql.QuestionHandler;
import table.Table;
import user.User;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户状态码：0--进入房间
 * @author 98267
 */
@ServerEndpoint(value="/playGame",configurator=GetHttpSessionConfigurator.class)
public class PlayGame {
    /**
     * 每页最多的桌子数目
     */
    private static final int MAX_TABLES_PER=8;
    /**
     * 当前状态下的最大页数
     */
    private static final int MAX_PAGES=10;
    /**
     * 桌子列表
     */
    private static List<Table> tables=new ArrayList<>();
    /**
     * 登陆状态的所有用户sessions
     */
    private static Map<String, Session> sessions = new HashMap<String, Session>();
    /**
     * 登陆状态的所有用户信息
     */
    private static Map<String, User> users = new HashMap<String, User>();
    /**
     * 处在大厅的session
     */
    private static List<String> gameLobbys=new ArrayList<>();
    /**
     * 离线玩家的列表（暂时没有用处，向后兼容）
     */
    private static Map<String,Integer> offLineUserIds=new HashMap<>();
    /**
     * 数据库链接器
     */
    private static BaseConnection baseConnection=new BaseConnection("ias");

    public PlayGame(){
        for(int i=tables.size();i<MAX_PAGES*MAX_TABLES_PER;i++){
            Table table=new Table();
            tables.add(table);
        }
    }

    @OnMessage
    public void onMessage(Session session, String msg){
        System.out.println(session.getId()+": message");
        JSONObject jsonObjectReceive=JSONObject.fromObject(msg);
        String command=jsonObjectReceive.getString("command");
        switch (command){
            //初始化当前的session
            case "init":{
                initWebsocket(session,jsonObjectReceive);
                break;
            }
            case "enterRoom":{
                enterRoom(session,jsonObjectReceive);
                break;
            }
            case "backToLobby":{
                if(backToLobby(session,jsonObjectReceive)){
                    sendInfoAllLobby("updateTableInfo");
                }
                break;
            }
            case "prepare":{
                playPrepare(session);
                break;
            }
            case "turnCrane":{
                turnCrane(session,jsonObjectReceive);
                break;
            }
            case "selectAnswer":{
                selectAnswer(session,jsonObjectReceive);
                break;
            }
            default:
                break;
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        System.out.println(session.getId()+": open");
        //获取httpSession
        HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        //当httpSession不存在的时候，应该先登陆
        if(httpSession==null){
            try {
                session.getBasicRemote().sendText("{\"commandBack\":\"exit\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            //当httpSession存在的时候，将其中的httpSession加入到users中
            User user=(User)httpSession.getAttribute("user");
            if(user!=null&&user.getUserId().length()==User.USER_ID_LENGTH){
                sessions.put(session.getId(),session);
                users.put(session.getId(),user);
                System.out.println("user login system;userId is "+user.getUserId());
            }
            else{
                try {
                    session.getBasicRemote().sendText("{\"commandBack\":\"exit\"}");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("user don't login system.");
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        System.out.println(session.getId()+" error");
        throwable.printStackTrace();
    }

    @OnClose
    public void onClose(Session session, CloseReason reason){
        User user=users.get(session.getId());
        if(user!=null){
            switch (user.getPlayGameCurrentPage()){
                case User.CURRENTPAGE_LOBBY:{
                    leaveGameLobby(session.getId());
                    sessions.remove(session.getId());
                    users.remove(session.getId());
                    System.out.println(session.getId()+": close;Lobby;当前在线人数:"+sessions.size());
                    break;
                }
                case User.CURRENTPAGE_PANEL:{
                    //当用户错误的关闭当前页面的时候
                    if(tables.get(user.getTableId()).userIdExisted(user.getUserId())){
                        tables.get(user.getTableId()).leaveRoom(session,user.getUserId());
                        sendInfoAllLobby("updateTableInfo");
                        sendUpgradeMember(user.getTableId());
                    }
                    //判断游戏是否开始
                    if(tables.get(user.getTableId()).isGameStart()){
                        //如果游戏已经开始
                        List offlineUserId=tables.get(user.getTableId()).getOfflineUserId();
                        offlineUserId.add(user.getUserId());
                        if(tables.get(user.getTableId()).getEnterSession().size()==0){
                            System.out.println("清理table:"+user.getTableId());
                            tables.set(user.getTableId(),(new Table()));
                        }
                    }
                    else{
                        //如果游戏没有开始
                        tables.get(user.getTableId()).getEnterSession().remove(session);
                        tables.get(user.getTableId()).getPrepareSession().remove(session);
                        sessions.remove(session.getId());
                        users.remove(session.getId());
                        System.out.println(session.getId()+": close;Panel");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }

    /**
     * 已经登陆的session根据页面初始化
     * @param session 已经登陆的session
     * @param jsonObject 前端传递过来的参数转化成的JSONObject
     * @return 是否没有问题的成功初始化
     */
    private static synchronized boolean initWebsocket(Session session,JSONObject jsonObject){
        boolean result=false;
        String position=jsonObject.getString("position");
        switch (position){
            case "gameLobby":{
                //设置user访问的状态
                if(users.get(session.getId())!=null){
                    users.get(session.getId()).setPlayGameCurrentPage(User.CURRENTPAGE_LOBBY);
                    gameLobbys.add(session.getId());
                    List<String> backResultList=new ArrayList<>();
                    Map backResultMap=new HashMap<>(16);
                    //传递table的列表信息
                    for(int i=0;i<tables.size();i++){
                        String tableBriefInfo=tables.get(i).stateToStr();
                        backResultList.add(tableBriefInfo);
                    }
                    backResultMap.put("commandBack","updateTableInfo");
                    backResultMap.put("data",backResultList);
                    JSONObject backResultJson=JSONObject.fromObject(backResultMap);
                    try {
                        session.getBasicRemote().sendText(backResultJson.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case "gamePanel":{
                int tableId=jsonObject.getInt("tableId");
                //当tableId不符合规范的时候
                if(tableId==Table.ERROR_ID){
                    try {
                        session.getBasicRemote().sendText("{\"commandBack\":\"backToLobby\"}");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                User user=users.get(session.getId());
                if(user!=null){
                    user.setPlayGameCurrentPage(User.CURRENTPAGE_PANEL);
                    user.setTableId(tableId);
                }
                else{
                    try {
                        session.getBasicRemote().sendText("{\"commandBack\":\"roomFull\"}");
                        return false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Table table=tables.get(tableId);
                //当table中没有加入的userId
                if(user!=null&&!table.userIdExisted(user.getUserId())){
                    try {
                        session.getBasicRemote().sendText("{\"commandBack\":\"roomFull\"}");
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(!table.enterRoom(session)){
                    try {
                        session.getBasicRemote().sendText("{\"commandBack\":\"backToLobby\"}");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    sendInfoAllLobby("updateTableInfo");
                    sendUpgradeMember(tableId);
                }
                break;
            }
            default:
                break;
        }
        return result;
    }

    /**
     * 将已经登陆的session加入到房间
     * @param session 已经登陆的session
     * @param jsonObject 前端传递过来的参数转化成的JSONObject
     * @return 是否没有问题的加入到房间
     */
    private static synchronized boolean enterRoom(Session session,JSONObject jsonObject){
        boolean result=false;
        int tableId=jsonObject.getInt("tableId");
        Table table=tables.get(tableId);
        Map backResultMap=new HashMap<>(16);
        backResultMap.put("commandBack","enterRoom");
        backResultMap.put("tableId",tableId);
        if(table.getEnterSession().size()<Table.MAX_POSITION){
            System.out.println(session.getId()+" : 加入成功");
            table.getEnterUserId().add(users.get(session.getId()).getUserId());
            backResultMap.put("enterRoomResult",true);
            leaveGameLobby(session.getId());
            result=true;
        }
        else{
            System.out.println(session.getId()+" : 加入失败");
            backResultMap.put("enterRoomResult",false);
        }
        JSONObject backResultJson=JSONObject.fromObject(backResultMap);
        try {
            session.getBasicRemote().sendText(backResultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 已经登陆并初始化后的session离开gameLobby界面的处理函数
     * @param sessionId 离开gameLobby界面的session的id
     * @return 返回是否没有错误地成功离开
     */
    private static synchronized boolean leaveGameLobby(String sessionId){
        boolean result=false;
        for(int i=0;i<gameLobbys.size();i++){
            if (gameLobbys.get(i).equals(sessionId)){
                gameLobbys.remove(i);
                result=true;
                break;
            }
        }
        return result;
    }

    /**
     * 发送给所有在大厅的人员的信息
     * @param type 发送给所有在大厅的人员的信息种类
     * @return 返回是否没有错误地成功发送给所有用户
     */
    private static synchronized boolean sendInfoAllLobby(String type){
        boolean result=false;
        switch (type){
            case "updateTableInfo":{
                //获取所有table的简要信息
                List<String> backResultList=new ArrayList<>();
                Map backResultMap=new HashMap<>(16);
                //传递table的列表信息
                for(int i=0;i<tables.size();i++){
                    String tableBriefInfo=tables.get(i).stateToStr();
                    backResultList.add(tableBriefInfo);
                }
                backResultMap.put("commandBack","updateTableInfo");
                backResultMap.put("data",backResultList);
                JSONObject backResultJson=JSONObject.fromObject(backResultMap);
                System.out.println(backResultJson.toString());
                try {
                    for(int i=0;i<gameLobbys.size();i++){
                        String sessionId=gameLobbys.get(i);
                        Session session=sessions.get(sessionId);
                        session.getBasicRemote().sendText(backResultJson.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            default:
                break;
        }
        return result;
    }

    /**
     * 将已经登陆的session回到大厅
     * @param session 需要回到大厅的session
     * @param jsonObject 前端传递过来的参数转化成的JSONObject
     * @return 是否没有问题的回到大厅
     */
    private static synchronized boolean backToLobby(Session session,JSONObject jsonObject){
        //将离开信息发送给离开的session
        boolean result=false;
        int tableId=jsonObject.getInt("tableId");
        Table table=tables.get(tableId);
        Map backResultMap=new HashMap<>(16);
        backResultMap.put("commandBack","backToLobby");
        if(table.leaveRoom(session,users.get(session.getId()).getUserId())){
            System.out.println(session.getId()+" : 离开成功");
            backResultMap.put("backToLobby",true);
            result=true;
        }
        JSONObject backResultJson=JSONObject.fromObject(backResultMap);
        try {
            session.getBasicRemote().sendText(backResultJson.toString());
            sendUpgradeMember(tableId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送给所有在游戏桌内的人员的信息
     * @param tableId 发送的游戏桌号码
     * @param type 发送给所有在游戏桌内的人员的信息种类
     * @param attachment 附带的信息
     * @return 返回是否没有错误地成功发送给所有用户
     */
    private static synchronized boolean sendInfoAllTable(int tableId,String type,String attachment){
        boolean result=false;
        switch (type){
            case "updateMemberInfo":{
                try {
                    if(attachment!=null){
                        for(int i=0;i<tables.get(tableId).getEnterSession().size();i++){
                            Session session=(Session)tables.get(tableId).getEnterSession().get(i);
                            session.getBasicRemote().sendText(attachment);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "startPlay":{
                try {
                    if(attachment!=null){
                        for(int i=0;i<tables.get(tableId).getEnterSession().size();i++){
                            Session session=(Session)tables.get(tableId).getEnterSession().get(i);
                            session.getBasicRemote().sendText(attachment);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "turnAngelResult":{
                try {
                    if(attachment!=null){
                        for(int i=0;i<tables.get(tableId).getEnterSession().size();i++){
                            Session session=(Session)tables.get(tableId).getEnterSession().get(i);
                            session.getBasicRemote().sendText(attachment);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "selectAnswer":{
                try {
                    if(attachment!=null){
                        for(int i=0;i<tables.get(tableId).getEnterSession().size();i++){
                            Session session=(Session)tables.get(tableId).getEnterSession().get(i);
                            session.getBasicRemote().sendText(attachment);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "turnAngelBlackHouse":{
                try {
                    if(attachment!=null){
                        for(int i=0;i<tables.get(tableId).getEnterSession().size();i++){
                            Session session=(Session)tables.get(tableId).getEnterSession().get(i);
                            session.getBasicRemote().sendText(attachment);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "success":{
                try {
                    if(attachment!=null){
                        for(int i=0;i<tables.get(tableId).getEnterSession().size();i++){
                            Session session=(Session)tables.get(tableId).getEnterSession().get(i);
                            session.getBasicRemote().sendText(attachment);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            default:
                break;
        }
        return result;
    }

    /**
     * 将table更新好的成员发送给table内部的所有成员
     * @param tableId
     * @return
     */
    private static synchronized boolean sendUpgradeMember(int tableId){
        boolean result;
        Table table=tables.get(tableId);
        List enterSession=table.getEnterSession();
        //将离开信息发送给所有在桌子内的玩家
        ArrayList backResultList=new ArrayList();
        ArrayList backResultListId=new ArrayList();
        HashMap backResultMap=new HashMap(16);
        for(int i=0;i<enterSession.size();i++){
            Session sessionInTable= (Session) enterSession.get(i);
            User user=users.get(sessionInTable.getId());
            String nickName=user.getNickname();
            String id=user.getUserId();
            backResultList.add(nickName);
            backResultListId.add(id);
        }
        backResultMap.put("commandBack","upgradeMember");
        backResultMap.put("data",backResultList);
        backResultMap.put("ids",backResultListId);
        JSONObject backResultJson=JSONObject.fromObject(backResultMap);
        result=sendInfoAllTable(tableId,"updateMemberInfo",backResultJson.toString());
        //发送每个session单独的sessionId
        try {
            for(int i=0;i<enterSession.size();i++){
                Session sessionInTable= (Session) enterSession.get(i);
                User user=users.get(sessionInTable.getId());
                String id=user.getUserId();
            sessionInTable.getBasicRemote().sendText("{\"commandBack\":\"setUserId\",\"data\":\""+id+"\"}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 当前玩家准备
     * @return 游戏是否开始
     */
    private static synchronized boolean playPrepare(Session session){
        boolean result=false;
        Table table=tables.get(users.get(session.getId()).getTableId());
        if(table.prepare(users.get(session.getId()).getUserId(),session)){
            //如果准备好了
            result=true;
            startPlay(users.get(session.getId()).getTableId());
        }
        else{
            //如果没有准备好
            String sendToSession="{\"commandBack\":\"prepareFinish\"}";
            try {
                session.getBasicRemote().sendText(sendToSession);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sendInfoAllLobby("updateTableInfo");
        return result;
    }

    /**
     * 开始游戏
     * @return 将开始游戏的消息发送给所有玩家
     */
    private static synchronized boolean startPlay(int tableId){
        boolean result=false;
        String sendToTables="{\"commandBack\":\"startPlay\"}";
        tables.get(tableId).initScores();
        tables.get(tableId).initTrace();
        tables.get(tableId).initBlack();
        sendInfoAllTable(tableId,"startPlay",sendToTables);
        return result;
    }

    /**
     * 转动转盘
     * @return 将转动转盘的结果分发给每一桌面用户，并且将题目也发送给每一个用户
     */
    private static synchronized boolean turnCrane(Session session,JSONObject jsonObject){
        boolean result=false;
        Table table=tables.get(users.get(session.getId()).getTableId());
        int userRoomPosition=table.getUserRoomPosition(users.get(session.getId()).getUserId());
        Map blackHouse=table.getBlackHouse();
        boolean isBlackHouse=(boolean)blackHouse.get(userRoomPosition);
        if(!isBlackHouse){
            //如果没有关在黑屋子里面

            //配置QuestionHandler和获取table
            QuestionHandler questionHandler=new QuestionHandler(baseConnection);

            //获取问题的种类
            List trace=table.getTracePosition();
            int roomUserPosition=table.getUserRoomPosition(users.get(session.getId()).getUserId());
            int newTracePosition=(int)trace.get(roomUserPosition)+Integer.valueOf((String)jsonObject.get("angel"));
            newTracePosition=newTracePosition%10;
            trace.set(roomUserPosition,newTracePosition);
            int questionType=newTracePosition%questionHandler.getTypeNum();

            //获取问题
            Question question=(Question)questionHandler.selectSQL(questionType);
            Map jsonResultMap=new HashMap(16);
            jsonResultMap.put("question",question);
            jsonResultMap.put("commandBack","turnAngelResult");
            jsonResultMap.put("angel",jsonObject.get("angel"));
            jsonResultMap.put("userId",users.get(session.getId()).getUserId());
            JSONObject jsonResultObject=JSONObject.fromObject(jsonResultMap);
            System.out.println(jsonResultObject.toString());
            String sendToTables=jsonResultObject.toString();
            sendInfoAllTable(users.get(session.getId()).getTableId(),"turnAngelResult",sendToTables);
        }
        else{
            //如果关在黑屋子里面
            String angel=(String)jsonObject.get("angel");
            int angelInteger=Integer.valueOf(angel);
            boolean outBlackHouse=false;
            if(angelInteger%2==0){
                outBlackHouse=true;
            }
            table.setBlackHouseValue(userRoomPosition,outBlackHouse);
            Map jsonResultMap=new HashMap(16);
            jsonResultMap.put("commandBack","turnAngelBlackHouse");
            jsonResultMap.put("angel",jsonObject.get("angel"));
            jsonResultMap.put("userId",users.get(session.getId()).getUserId());
            jsonResultMap.put("outBlackHouse",outBlackHouse);
            jsonResultMap.put("turn",table.getCurrentTurn());
            JSONObject jsonResultObject=JSONObject.fromObject(jsonResultMap);
            System.out.println(jsonResultObject.toString());
            String sendToTables=jsonResultObject.toString();
            sendInfoAllTable(users.get(session.getId()).getTableId(),"turnAngelBlackHouse",sendToTables);
        }
        return result;
    }

    /**
     *
     * @param session
     * @param jsonObject
     * @return
     */
    private static synchronized boolean selectAnswer(Session session,JSONObject jsonObject){
        boolean result=false;
        Table table=tables.get((int)jsonObject.get("tableId"));

            boolean answerResult=(boolean)jsonObject.get("result");
            int answer=(int)jsonObject.get("answer");
            User user=users.get(session.getId());
            int positionId=table.getUserRoomPosition(user.getUserId());

            table.setSelectedAnswer(positionId,answerResult);
        Integer someSuccess=table.hasSomeSuccess();
        if(someSuccess==null){
            HashMap jsonBackResult=new HashMap(16);
            jsonBackResult.put("commandBack","selectResult");
            jsonBackResult.put("userId",user.getUserId());
            jsonBackResult.put("scores",table.getScores());
            jsonBackResult.put("result",answerResult);
            jsonBackResult.put("answer",answer);
            jsonBackResult.put("turn",table.getCurrentTurn());
            JSONObject jsonBackObject=JSONObject.fromObject(jsonBackResult);
            System.out.println(jsonBackObject.toString());
            sendInfoAllTable(users.get(session.getId()).getTableId(),"selectAnswer",jsonBackObject.toString());
            return result;
        }
        else{
            table.againPlay();
            HashMap jsonBackResult=new HashMap(16);
            jsonBackResult.put("commandBack","success");
            jsonBackResult.put("positionId",String.valueOf(someSuccess));
            JSONObject jsonBackObject=JSONObject.fromObject(jsonBackResult);
            System.out.println(jsonBackObject.toString());
            sendInfoAllTable(users.get(session.getId()).getTableId(),"success",jsonBackObject.toString());
            return result;
        }
    }
}
