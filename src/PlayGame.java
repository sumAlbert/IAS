import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 98267
 */
@ServerEndpoint(value="/playGame")
public class PlayGame {
    private static Map<String, Session> sessions = new HashMap<String, Session>();

    @OnMessage
    public void onMessage(Session session, String msg){
        System.out.println("message");
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        System.out.println("open");
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        System.out.println("error");

    }

    @OnClose
    public void onClose(Session session, CloseReason reason){
        System.out.println("close");

    }
}
