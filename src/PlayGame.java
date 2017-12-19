import net.sf.json.JSONObject;
import table.Table;

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
@ServerEndpoint(value="/playGame")
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

    public PlayGame(){
        for(int i=1;i<=MAX_PAGES*MAX_TABLES_PER;i++){
            Table table=new Table();
            table.setNum(i);
            tables.add(table);
        }
    }


    @OnMessage
    public void onMessage(Session session, String msg){
        System.out.println(session.getId()+": message");
        JSONObject jsonObjectReceive=JSONObject.fromObject(msg);
        int commandId=jsonObjectReceive.getInt("commandId");
        switch (commandId){
            case 0:{
                enterRoom(session,jsonObjectReceive);
                break;
            }
            default:
                break;
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        System.out.println(session.getId()+": open");

        sessions.put(session.getId(),session);

    }

    @OnError
    public void onError(Session session, Throwable throwable){
        System.out.println(session.getId()+": error");
    }

    @OnClose
    public void onClose(Session session, CloseReason reason){
        sessions.remove(session.getId());
        System.out.println(session.getId()+": close || The size of sessions"+sessions.size());
    }

    private boolean enterRoom(Session session,JSONObject jsonObject){
        boolean result=false;
        return result;
    }
}
