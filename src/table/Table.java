package table;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 98267
 */
public class Table {
    public static final int STATE_EMPTY=0;
    public static final int STATE_WAITING=1;
    public static final int STATE_GAMEING=2;
    public static final int MAX_POSITION=4;
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
        if(enterSession.size()<Table.MAX_POSITION){
            this.enterSession.add(session);
            this.state=Table.STATE_WAITING;
            result=true;
        }
        return result;
    };
}
