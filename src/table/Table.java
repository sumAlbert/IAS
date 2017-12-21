package table;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

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
}





