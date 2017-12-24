package table;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 98267
 */
public class Table {
    /**
     * table的状态
     */
    public static final int STATE_EMPTY=0;
    public static final int STATE_WAITING=1;
    public static final int STATE_GAMEING=2;
    /**
     * 最大座位数
     */
    public static final int MAX_POSITION=4;
    /**
     * 错误的roomId
     */
    public static final int ERROR_ID=999999;
    /**
     * Table的状态
     */
    private int state=Table.STATE_EMPTY;
    /**
     * 准备好的客户端
     */
    private List prepareSession=new ArrayList<Session>();
    /**
     * 加入的客户端
     */
    private List enterSession=new ArrayList<Session>();
    /**
     * 登录到该房间的user,用于用户刷新页面
     */
    private List enterUserId=new ArrayList();
    /**
     * 离线用户的userId
     */
    private List offlineUserId=new ArrayList();
    /**
     * 游戏是否开始
     */
    private boolean gameStart=false;
    /**
     * 当前轮到哪个座位上的人
     */
    private int currentTurn=0;
    /**
     * 记录各个位置的分数
     */
    private List scores=new ArrayList();
    /**
     * 跑到上的位置
     */
    private List tracePosition=new ArrayList();
    /**
     * 小黑屋
     */
    private Map blackHouse=new HashMap();


    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public List getEnterSession() {
        return enterSession;
    }
    public List getPrepareSession() {
        return prepareSession;
    }
    public void setEnterSession(List enterSession) {
        this.enterSession = enterSession;
    }
    public void setPrepareSession(List prepareSession) {
        this.prepareSession = prepareSession;
    }
    public void setEnterUserId(List enterUserId) {
        this.enterUserId = enterUserId;
    }
    public List getEnterUserId() {
        return enterUserId;
    }
    public boolean isGameStart() {
        return gameStart;
    }
    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }
    public List getOfflineUserId() {
        return offlineUserId;
    }
    public void setOfflineUserId(List offlineUserId) {
        this.offlineUserId = offlineUserId;
    }
    public int getCurrentTurn() {
        return currentTurn;
    }
    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }
    public void setScores(List scores) {
        this.scores = scores;
    }
    public List getScores() {
        return scores;
    }
    public List getTracePosition() {
        return tracePosition;
    }
    public void setTracePosition(List tracePosition) {
        this.tracePosition = tracePosition;
    }

    /**
     * 将房间的状态信息转换成json字符串
     * @return 房间的状态信息
     */
    public String stateToStr(){
        return "{\"state\":\""+this.state+"\",\"prepareNum\":\""+prepareSession.size()+"\",\"enterNum\":\""+enterSession.size()+"\"}";
    }
    /**
     * 将一个已经登陆的用户加入到房间
     * @param session 准备加入到房间的session
     * @return 是否没有错误的加入房间成功
     */
    public boolean enterRoom(Session session){
        boolean result=false;
        if(enterSession.size()<Table.MAX_POSITION&&(!sessionExisted(session))){
            this.enterSession.add(session);
            this.state=Table.STATE_WAITING;
            result=true;
        }
        return result;
    }
    /**
     * 让一个已经登陆的用户离开房间
     * @param session 准备离开房间的session
     * @return 是否没有错误的离开房间
     */
    public boolean leaveRoom(Session session,String userId){
        boolean result=this.enterSession.remove(session);
        this.prepareSession.remove(session);
        this.enterUserId.remove(userId);
        if(enterSession.size()==0) {
            this.state = Table.STATE_EMPTY;
        }
        return result;
    }
    /**
     * 判断UserId是否存在
     * @param userId 判断的UserId
     * @return 结果
     */
    public boolean userIdExisted(String userId){
        boolean result=false;
        for (Object anEnterUserId : enterUserId) {
            if (userId.equals(anEnterUserId)) {
                result = true;
            }
        }
        return result;
    }
    /**
     * 判断session是否存在
     * @param session 判断的session
     * @return 结果
     */
    private boolean sessionExisted(Session session){
        boolean result=false;
        for (Object anEnterSession : enterSession) {
            if (session.equals(anEnterSession)) {
                result = true;
            }
        }
        return result;
    }
    /**
     * userId准备游戏
     * @param userId 准备游戏的userId
     * @return 游戏是否开始
     */
    public boolean prepare(String userId,Session session){
        boolean result=false;
        if(!gameStart){
            if(userIdExisted(userId)){
                prepareSession.remove(session);
                prepareSession.add(session);
                if(prepareSession.size()==enterSession.size()&&prepareSession.size()>1){
                    gameStart=true;
                    this.state=Table.STATE_GAMEING;
                    result=true;
                }
            }
        }
        return result;
    }
    /**
     * 初始化分数
     */
    public void initScores(){
        this.scores=new ArrayList();
        scores.add(0);
        scores.add(0);
        scores.add(0);
        scores.add(0);
    }
    /**
     * 初始化在跑道上的位置
     */
    public void initTrace(){
        this.tracePosition=new ArrayList();
        tracePosition.add(0);
        tracePosition.add(0);
        tracePosition.add(0);
        tracePosition.add(0);
    }
    /**
     * 初始化小黑屋里面的人
     */
    public void initBlack(){
        this.blackHouse=new HashMap<>(16);
        this.blackHouse.put(0,false);
        this.blackHouse.put(1,false);
        this.blackHouse.put(2,false);
        this.blackHouse.put(3,false);
    }
    /**
     * 获取用户所在的位置
     * @param userId 用户的id
     * @return
     */
    public int getUserRoomPosition(String userId){
        int result=1000;
        for(int i=0;i<this.enterUserId.size();i++){
            if(enterUserId.get(i).equals(userId)){
                result=i;
            }
        }
        return result;
    }

    /**
     * 选中答案以后的设置
     * @param position 选择答案的玩家的位置
     * @param result 选择的答案是否正确
     */
    public void setSelectedAnswer(int position,boolean result){
        if(result){
            int score=(int)this.scores.get(position);
            this.scores.set(position,score+1);
        }
        else{
            this.blackHouse.put(position,true);
        }
        this.currentTurn=(this.currentTurn+1)%enterSession.size();
    }
}





