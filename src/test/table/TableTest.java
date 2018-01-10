package test.table; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import table.Table;
import user.User;

import javax.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.*;

import static org.junit.Assert.*;

/** 
* Table Tester. 
* 
* @author <Authors name> 
* @since <pre>一月 1, 2018</pre> 
* @version 1.0 
*/ 
public class TableTest {
    User user1 = null;
    User user2 = null;
    User user3 = null;
    User user4 = null;
    Table table = null;
    Session session1 = null;
    Session session2 = null;

@Before
public void before() throws Exception {
    user1 = new User("zhangsan", "zhangsan","123456");
    user1.setUserId("1");
    user2 = new User("lisi", "lisi","123456");
    user2.setUserId("2");
    user3 = new User("wangwu", "wangwu", "123456");
    user3.setUserId("3");
    user4 = new User("zhaoliu", "zhaoliu", "123456");
    table = new Table();
    session1 = new Session() {
        @Override
        public WebSocketContainer getContainer() {
            return null;
        }

        @Override
        public void addMessageHandler(MessageHandler messageHandler) throws IllegalStateException {

        }

        @Override
        public Set<MessageHandler> getMessageHandlers() {
            return null;
        }

        @Override
        public void removeMessageHandler(MessageHandler messageHandler) {

        }

        @Override
        public String getProtocolVersion() {
            return null;
        }

        @Override
        public String getNegotiatedSubprotocol() {
            return null;
        }

        @Override
        public List<Extension> getNegotiatedExtensions() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public long getMaxIdleTimeout() {
            return 0;
        }

        @Override
        public void setMaxIdleTimeout(long l) {

        }

        @Override
        public void setMaxBinaryMessageBufferSize(int i) {

        }

        @Override
        public int getMaxBinaryMessageBufferSize() {
            return 0;
        }

        @Override
        public void setMaxTextMessageBufferSize(int i) {

        }

        @Override
        public int getMaxTextMessageBufferSize() {
            return 0;
        }

        @Override
        public RemoteEndpoint.Async getAsyncRemote() {
            return null;
        }

        @Override
        public RemoteEndpoint.Basic getBasicRemote() {
            return null;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public void close(CloseReason closeReason) throws IOException {

        }

        @Override
        public URI getRequestURI() {
            return null;
        }

        @Override
        public Map<String, List<String>> getRequestParameterMap() {
            return null;
        }

        @Override
        public String getQueryString() {
            return null;
        }

        @Override
        public Map<String, String> getPathParameters() {
            return null;
        }

        @Override
        public Map<String, Object> getUserProperties() {
            return null;
        }

        @Override
        public Principal getUserPrincipal() {
            return null;
        }

        @Override
        public Set<Session> getOpenSessions() {
            return null;
        }

        @Override
        public <T> void addMessageHandler(Class<T> aClass, MessageHandler.Partial<T> partial) throws IllegalStateException {

        }

        @Override
        public <T> void addMessageHandler(Class<T> aClass, MessageHandler.Whole<T> whole) throws IllegalStateException {

        }
    };
    session2 = new Session() {
        @Override
        public WebSocketContainer getContainer() {
            return null;
        }

        @Override
        public void addMessageHandler(MessageHandler messageHandler) throws IllegalStateException {

        }

        @Override
        public Set<MessageHandler> getMessageHandlers() {
            return null;
        }

        @Override
        public void removeMessageHandler(MessageHandler messageHandler) {

        }

        @Override
        public String getProtocolVersion() {
            return null;
        }

        @Override
        public String getNegotiatedSubprotocol() {
            return null;
        }

        @Override
        public List<Extension> getNegotiatedExtensions() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public long getMaxIdleTimeout() {
            return 0;
        }

        @Override
        public void setMaxIdleTimeout(long l) {

        }

        @Override
        public void setMaxBinaryMessageBufferSize(int i) {

        }

        @Override
        public int getMaxBinaryMessageBufferSize() {
            return 0;
        }

        @Override
        public void setMaxTextMessageBufferSize(int i) {

        }

        @Override
        public int getMaxTextMessageBufferSize() {
            return 0;
        }

        @Override
        public RemoteEndpoint.Async getAsyncRemote() {
            return null;
        }

        @Override
        public RemoteEndpoint.Basic getBasicRemote() {
            return null;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public void close(CloseReason closeReason) throws IOException {

        }

        @Override
        public URI getRequestURI() {
            return null;
        }

        @Override
        public Map<String, List<String>> getRequestParameterMap() {
            return null;
        }

        @Override
        public String getQueryString() {
            return null;
        }

        @Override
        public Map<String, String> getPathParameters() {
            return null;
        }

        @Override
        public Map<String, Object> getUserProperties() {
            return null;
        }

        @Override
        public Principal getUserPrincipal() {
            return null;
        }

        @Override
        public Set<Session> getOpenSessions() {
            return null;
        }

        @Override
        public <T> void addMessageHandler(Class<T> aClass, MessageHandler.Partial<T> partial) throws IllegalStateException {

        }

        @Override
        public <T> void addMessageHandler(Class<T> aClass, MessageHandler.Whole<T> whole) throws IllegalStateException {

        }
    };
} 

@After

/** 
* 
* Method: stateToStr() 
* 
*/ 
@Test
public void testStateToStr() throws Exception { 
//TODO: Test goes here...
    assertEquals("{\"state\":\""+"0"+"\",\"prepareNum\":\""+"0"+"\",\"enterNum\":\""+"0"+"\"}", "{\"state\":\""+table.getState()+"\",\"prepareNum\":\""+table.getPrepareSession().size()+"\",\"enterNum\":\""+table.getEnterSession().size()+"\"}");
}

/** 
* 
* Method: enterRoom(Session session) 
* 
*/

@Test
public void testGetState() {
    assertEquals(0,table.getState());
}

@Test
public void testSetState() {
    table.setState(1);
    assertEquals(1,table.getState());
    table.setState(0);
}

@Test
public void testGetEnterSession() {
    table.getEnterSession().add(session1);
    table.getEnterSession().add(session2);
    assertEquals(2,table.getEnterSession().size());
    table.getEnterSession().remove(session1);
    table.getEnterSession().remove(session2);
}

@Test
public void testGetPrepareSession() {
    table.getPrepareSession().add(session1);
    table.getPrepareSession().add(session2);
    assertEquals(2,table.getPrepareSession().size());
    table.getPrepareSession().remove(session1);
    table.getPrepareSession().remove(session2);
}

@Test
public void testSetEnterSession() {
    List testEnterSession = new ArrayList<Session>();
    testEnterSession.add(session1);
    table.setEnterSession(testEnterSession);
    assertEquals(1, table.getEnterSession().size());
    table.setEnterSession(new ArrayList<Session>());
}

@Test
public void testSetPrepareSession() {
    List testPrepareSession = new ArrayList<Session>();
    testPrepareSession.add(session2);
    table.setPrepareSession(testPrepareSession);
    assertEquals(1, table.getPrepareSession().size());
    table.setPrepareSession(new ArrayList<Session>());
}

@Test
public void testSetEnterUserId() {
    List testEnterUserId = new ArrayList<String>();
    testEnterUserId.add(user1.getUserId());
    table.setEnterUserId(testEnterUserId);
    assertTrue(table.getEnterUserId().contains(user1.getUserId()));
    table.setEnterUserId(new ArrayList());
}

@Test
public void testGetEnterUserId() {
    table.getEnterUserId().add(user1.getUserId());
    assertTrue(table.getEnterUserId().contains(user1.getUserId()));
    table.getEnterUserId().remove(user1.getUserId());
}

@Test
public void testIsGameStart() {
    assertFalse(table.isGameStart());
}

@Test
public void testSetGameStart() {
    table.setGameStart(true);
    assertTrue(table.isGameStart());
    table.setGameStart(false);
}

@Test
public void testGetOfflineUserId() {
    List testOfflineUser = new ArrayList();
    testOfflineUser.add(user2.getUserId());
    table.setOfflineUserId(testOfflineUser);
    assertTrue(table.getOfflineUserId().contains(user2.getUserId()));
    table.setOfflineUserId(new ArrayList());
}

@Test
public void testSetOfflineUserId() {
    List testOfflineUser = new ArrayList();
    testOfflineUser.add(user2.getUserId());
    table.setOfflineUserId(testOfflineUser);
    assertEquals(1,table.getOfflineUserId().size());
    table.getOfflineUserId().remove(user2.getUserId());
}

@Test
public void testGetCurrentTurn() {
    table.setCurrentTurn(1);
    assertEquals(1,table.getCurrentTurn());
    table.setCurrentTurn(0);
}

@Test
public void testSetCurrent() {
    table.setCurrentTurn(1);
    assertEquals(1,table.getCurrentTurn());
    table.setCurrentTurn(0);
}

@Test
public void testSetScores() {
    List testScores = new ArrayList();
    testScores.add(0);
    testScores.add(1);
    testScores.add(0);
    testScores.add(0);
    table.setScores(testScores);
    assertEquals(1,table.getScores().indexOf(1));
    table.setScores(new ArrayList());
}

@Test
public void testGetScores() {
    List testScores = new ArrayList();
    testScores.add(0);
    testScores.add(1);
    testScores.add(0);
    testScores.add(0);
    table.setScores(testScores);
    assertEquals(1,table.getScores().indexOf(1));
    table.setScores(new ArrayList());
}

@Test
public void testGetTracePosition() {
    List tracePosition = new ArrayList();
    tracePosition.add(0);
    tracePosition.add(0);
    tracePosition.add(1);
    tracePosition.add(0);
    table.setTracePosition(tracePosition);
    assertEquals(1,table.getTracePosition().get(2));
    table.setTracePosition(new ArrayList());
}

@Test
public void testSetTrancePosition() {
    List tracePosition = new ArrayList();
    tracePosition.add(0);
    tracePosition.add(0);
    tracePosition.add(1);
    tracePosition.add(0);
    table.setTracePosition(tracePosition);
    assertEquals(1,table.getTracePosition().get(2));
    table.setTracePosition(new ArrayList());
}

@Test
public void testGetBlackHouse() {
    table.initBlack();
    assertTrue(table.getBlackHouse().containsKey(3));
    table.setBlackHouse(new HashMap());
}

@Test
public void testSetBlackHouse() {
    Map blackHouse = new HashMap();
    blackHouse.put(0,false);
    blackHouse.put(1,false);
    blackHouse.put(2,true);
    blackHouse.put(3,false);
    table.setBlackHouse(blackHouse);
    assertEquals(true,table.getBlackHouse().get(2));
    table.setBlackHouse(new HashMap());
}

@Test
public void testEnterRoom() throws Exception { 
//TODO: Test goes here...
    table.getEnterSession().add(session1);
    assertTrue(table.enterRoom(session2));
    assertTrue(table.getEnterSession().contains(session2));
    assertEquals(1, table.getState());
    table.getEnterSession().remove(session1);
    table.getEnterSession().remove(session2);
    table.setState(0);
} 

/** 
* 
* Method: leaveRoom(Session session, String userId) 
* 
*/ 
@Test
public void testLeaveRoom() throws Exception { 
//TODO: Test goes here...
    table.getEnterSession().add(session1);
    table.getEnterSession().add(session2);
    table.getEnterUserId().add(user1.getUserId());
    table.getEnterUserId().add(user2.getUserId());
    table.setState(1);
    assertTrue(table.leaveRoom(session1, user1.getUserId()));
    assertFalse(table.getEnterSession().contains(session1));
    assertFalse(table.getEnterUserId().contains(user1.getUserId()));
    assertEquals(1, table.getState());
    assertTrue(table.leaveRoom(session2, user2.getUserId()));
    assertFalse(table.getEnterSession().contains(session2));
    assertFalse(table.getEnterUserId().contains(user2.getUserId()));
    assertEquals(0, table.getState());
    assertTrue(table.getEnterUserId().isEmpty());
    assertTrue(table.getEnterSession().isEmpty());
    assertFalse(table.leaveRoom(session1, user1.getUserId()));
} 

/** 
* 
* Method: userIdExisted(String userId) 
* 
*/ 
@Test
public void testUserIdExisted() throws Exception { 
//TODO: Test goes here...
    table.getEnterUserId().add(user1.getUserId());
    assertTrue(table.userIdExisted(user1.getUserId()));
    table.getEnterUserId().add(user2.getUserId());
    assertTrue(table.userIdExisted(user2.getUserId()));
    table.getEnterUserId().remove(user1.getUserId());
    table.getEnterUserId().remove(user2.getUserId());
} 

/** 
* 
* Method: userIdOffline(String userId) 
* 
*/ 
@Test
public void testUserIdOffline() throws Exception { 
//TODO: Test goes here...
    table.getOfflineUserId().add(user3.getUserId());
    assertTrue(table.userIdOffline(user3.getUserId()));
    table.getOfflineUserId().add(user4.getUserId());
    assertTrue(table.userIdOffline(user4.getUserId()));
    table.getOfflineUserId().remove(user1.getUserId());
    table.getOfflineUserId().remove(user2.getUserId());
} 

/** 
* 
* Method: prepare(String userId, Session session) 
* 
*/ 
@Test
public void get_prepare_but_userId_not_exist_then_return_false() {
//TODO: Test goes here...
    assertFalse(table.prepare(user1.getUserId(), new Session() {
        @Override
        public WebSocketContainer getContainer() {
            return null;
        }

        @Override
        public void addMessageHandler(MessageHandler messageHandler) throws IllegalStateException {

        }

        @Override
        public Set<MessageHandler> getMessageHandlers() {
            return null;
        }

        @Override
        public void removeMessageHandler(MessageHandler messageHandler) {

        }

        @Override
        public String getProtocolVersion() {
            return null;
        }

        @Override
        public String getNegotiatedSubprotocol() {
            return null;
        }

        @Override
        public List<Extension> getNegotiatedExtensions() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public long getMaxIdleTimeout() {
            return 0;
        }

        @Override
        public void setMaxIdleTimeout(long l) {

        }

        @Override
        public void setMaxBinaryMessageBufferSize(int i) {

        }

        @Override
        public int getMaxBinaryMessageBufferSize() {
            return 0;
        }

        @Override
        public void setMaxTextMessageBufferSize(int i) {

        }

        @Override
        public int getMaxTextMessageBufferSize() {
            return 0;
        }

        @Override
        public RemoteEndpoint.Async getAsyncRemote() {
            return null;
        }

        @Override
        public RemoteEndpoint.Basic getBasicRemote() {
            return null;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public void close(CloseReason closeReason) throws IOException {

        }

        @Override
        public URI getRequestURI() {
            return null;
        }

        @Override
        public Map<String, List<String>> getRequestParameterMap() {
            return null;
        }

        @Override
        public String getQueryString() {
            return null;
        }

        @Override
        public Map<String, String> getPathParameters() {
            return null;
        }

        @Override
        public Map<String, Object> getUserProperties() {
            return null;
        }

        @Override
        public Principal getUserPrincipal() {
            return null;
        }

        @Override
        public Set<Session> getOpenSessions() {
            return null;
        }

        @Override
        public <T> void addMessageHandler(Class<T> aClass, MessageHandler.Partial<T> partial) throws IllegalStateException {

        }

        @Override
        public <T> void addMessageHandler(Class<T> aClass, MessageHandler.Whole<T> whole) throws IllegalStateException {

        }
    }));
}

@Test
public void get_prepare_and_the_user_id_exixts_but_the_size_of_prepare_session_not_equal_to_the_enter_session_then_return_false() {
    //TODO: Test goes here...
    table.getEnterUserId().add(user1.getUserId());
    table.getPrepareSession().add(session1);
    table.getEnterSession().add(session1);
    table.getEnterSession().add(session2);
    assertFalse(table.prepare(user1.getUserId(), session1));
    table.getEnterUserId().remove(user1.getUserId());
    table.getPrepareSession().remove(session1);
    table.getEnterSession().remove(session1);
    table.getEnterSession().remove(session2);
}

@Test
public void get_prepare_and_the_user_id_exixts_while_the_size_of_prepare_session_equal_to_the_enter_session_and_the_number_of_them_is_greater_than_1_then_return_true() {
    //TODO: Test goes here...
    table.getEnterUserId().add(user1.getUserId());
    table.getPrepareSession().add(session1);
    table.getPrepareSession().add(session2);
    table.getEnterSession().add(session1);
    table.getEnterSession().add(session2);
    assertTrue(table.prepare(user1.getUserId(), session1));
    table.getEnterUserId().remove(user1.getUserId());
    table.getPrepareSession().remove(session1);
    table.getPrepareSession().remove(session2);
    table.getEnterSession().remove(session1);
    table.getEnterSession().remove(session2);
    table.setState(0);
}
/**
* 
* Method: initScores() 
* 
*/ 
@Test
public void testInitScores() throws Exception { 
//TODO: Test goes here...
    table.initScores();
    assertEquals(0, table.getScores().get(0));
    assertEquals(0, table.getScores().get(1));
    assertEquals(0, table.getScores().get(2));
    assertEquals(0, table.getScores().get(3));
    assertEquals(4, table.getScores().size());
} 

/** 
* 
* Method: initTrace() 
* 
*/ 
@Test
public void testInitTrace() throws Exception { 
//TODO: Test goes here...
    table.initTrace();
    assertEquals(0, table.getTracePosition().get(0));
    assertEquals(0, table.getTracePosition().get(1));
    assertEquals(0, table.getTracePosition().get(2));
    assertEquals(0, table.getTracePosition().get(3));
    assertEquals(4, table.getTracePosition().size());
} 

/** 
* 
* Method: initBlack() 
* 
*/ 
@Test
public void testInitBlack() throws Exception { 
//TODO: Test goes here...
    table.initBlack();
    assertEquals(4, table.getBlackHouse().size());
    assertEquals(false, table.getBlackHouse().get(0));
    assertEquals(false, table.getBlackHouse().get(1));
    assertEquals(false, table.getBlackHouse().get(2));
    assertEquals(false, table.getBlackHouse().get(3));
} 

/** 
* 
* Method: getUserRoomPosition(String userId) 
* 
*/ 
@Test
public void testGetUserRoomPosition() throws Exception { 
//TODO: Test goes here...
    table.getEnterUserId().add(user1.getUserId());
    table.getEnterUserId().add(user3.getUserId());
    table.getEnterUserId().add(user2.getUserId());
    table.getEnterUserId().add(user4.getUserId());
    assertEquals(0, table.getUserRoomPosition(user1.getUserId()));
    assertEquals(2, table.getUserRoomPosition(user2.getUserId()));
    table.getEnterUserId().remove(user1.getUserId());
    table.getEnterUserId().remove(user3.getUserId());
    table.getEnterUserId().remove(user2.getUserId());
    table.getEnterUserId().remove(user4.getUserId());

} 

/** 
* 
* Method: setSelectedAnswer(int position, boolean result) 
* 
*/ 
@Test
public void the_position_0_user_answers_correctly_and_his_score_plus_one() {
//TODO: Test goes here...
    table.initScores();
    table.setSelectedAnswer(0, true);
    assertEquals(1, table.getScores().get(0));
    table.initScores();
}

@Test
public void the_position_0_user_fails_to_answer_and_into_the_blackhouse() {
    table.initBlack();
    table.setSelectedAnswer(0, false);
    assertEquals(true, table.getBlackHouse().get(0));
    table.initBlack();
}

/** 
* 
* Method: setBlackHouseValue(int position, boolean result) 
* 
*/ 
@Test
public void testSetBlackHouseValue() throws Exception { 
//TODO: Test goes here...
    table.getEnterSession().add(session1);
    table.getEnterSession().add(session2);
    table.setBlackHouseValue(0,false);
    assertEquals(true, table.getBlackHouse().get(0));
    assertEquals(1, table.getCurrentTurn());
    table.getEnterSession().remove(session1);
    table.getEnterSession().remove(session2);
    table.setCurrentTurn(0);
} 

/** 
* 
* Method: hasSomeSuccess() 
* 
*/ 
@Test
public void testHasSomeSuccess() throws Exception { 
//TODO: Test goes here...
    table.initScores();
    assertEquals(null, table.hasSomeSuccess());
    table.getScores().set(2,6);
    assertEquals(Integer.toString(2), Integer.toString(table.hasSomeSuccess()));
    table.initScores();
}

/** 
* 
* Method: againPlay() 
* 
*/ 
@Test
public void testAgainPlay() throws Exception { 
//TODO: Test goes here...
    table.setState(2);
    table.getPrepareSession().add(session1);
    table.getOfflineUserId().add(user1.getUserId());
    table.setGameStart(true);
    table.setCurrentTurn(2);
    table.againPlay();
    assertEquals(0, table.getTracePosition().get(0));
    assertEquals(0, table.getTracePosition().get(1));
    assertEquals(0, table.getTracePosition().get(2));
    assertEquals(0, table.getTracePosition().get(3));
    assertEquals(4, table.getTracePosition().size());
    assertEquals(0, table.getTracePosition().get(0));
    assertEquals(0, table.getTracePosition().get(1));
    assertEquals(0, table.getTracePosition().get(2));
    assertEquals(0, table.getTracePosition().get(3));
    assertEquals(4, table.getTracePosition().size());
    assertEquals(4, table.getBlackHouse().size());
    assertEquals(false, table.getBlackHouse().get(0));
    assertEquals(false, table.getBlackHouse().get(1));
    assertEquals(false, table.getBlackHouse().get(2));
    assertEquals(false, table.getBlackHouse().get(3));
    assertEquals(1, table.getState());
    assertEquals(0, table.getPrepareSession().size());
    assertEquals(0, table.getOfflineUserId().size());
    assertEquals(false, table.isGameStart());
    assertEquals(0, table.getCurrentTurn());
    table.setState(0);
} 



} 
