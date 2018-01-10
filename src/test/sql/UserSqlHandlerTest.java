package test.sql; 

import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import sql.BaseConnection;
import sql.QuestionHandler;
import sql.UserSqlHandler;
import user.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static org.junit.Assert.*;

/** 
* UserSqlHandler Tester. 
* 
* @author <Authors name> 
* @since <pre>一月 2, 2018</pre> 
* @version 1.0 
*/ 
public class UserSqlHandlerTest {
    private BaseConnection baseConnection = null;
    private Connection connection = null;
    private Statement statement = null;
    private User user1 = null;
    private User userManager = null;
    private UserSqlHandler userSqlHandler = null;
@Before
public void before() throws Exception {
    baseConnection = new BaseConnection("ias");
    userSqlHandler = new UserSqlHandler(baseConnection);
    user1 = new User("olala", "olala", "123456");
    userManager = new User("ll", "ll@qq.com", "7c4a8d09ca3762af61e59520943dc26494f8941b");
    user1.setUserId("111111");
} 

@After
public void after() throws Exception {
} 

/** 
* 
* Method: insertSQL(Object object) 
* 
*/ 
@Test
public void testInsertSQL() throws Exception { 
//TODO: Test goes here...
    connection = baseConnection.getConnection();
    statement = connection.createStatement();
    boolean isInserted = false;
    userSqlHandler.insertSQL(user1);
    ResultSet rs = statement.executeQuery("select * from users");
    while(rs.next()) {
        if(rs.getString("userId").equals("111111")) {
            isInserted = true;
        }
    }
    assertTrue(isInserted);
    statement.executeUpdate("delete from users where userId = '111111'");
    connection.close();
}

/** 
* 
* Method: selectSQL(Object object) 
* 
*/ 
@Test
public void testSelectSQL() throws Exception { 
//TODO: Test goes here...
    connection = baseConnection.getConnection();
    statement = connection.createStatement();
    assertFalse(Boolean.parseBoolean(userSqlHandler.selectSQL(user1).toString()));
    userSqlHandler.insertSQL(user1);
    assertTrue(Boolean.parseBoolean(userSqlHandler.selectSQL(user1).toString()));
    statement.executeUpdate("delete from users where userId = '111111'");
    connection.close();
} 

/** 
* 
* Method: selectAllSQL() 
* 
*/ 
@Test
public void testSelectAllSQL() throws Exception { 
//TODO: Test goes here...
    List<User> users = userSqlHandler.selectAllSQL();
    boolean isAllZero = true;
    int size = users.size();
    assertEquals(21,size);
} 

/** 
* 
* Method: deleteUsersId(List usersId) 
* 
*/ 
@Test
public void testDeleteUsersId() throws Exception { 
//TODO: Test goes here...
    userSqlHandler.insertSQL(user1);
    assertTrue(Boolean.parseBoolean(userSqlHandler.selectSQL(user1).toString()));
    List<String> userId = new ArrayList<>();
    userId.add(user1.getUserId());
    userSqlHandler.deleteUsersId(userId);
    assertFalse(Boolean.parseBoolean(userSqlHandler.selectSQL(user1).toString()));
} 

/** 
* 
* Method: updateUsersId(List users) 
* 
*/ 
@Test
public void testUpdateUsersId() throws Exception { 
//TODO: Test goes here...
    String str = null;
    connection = baseConnection.getConnection();
    statement = connection.createStatement();
    ResultSet rs = statement.executeQuery("select nickname from users where userId = 'user180108194823qevo'");
    while (rs.next()) {
        str = rs.getString("nickname");
    }
    assertEquals("LiXiaoLu", str);
    JSONObject jsonObject = JSONObject.fromObject("{\"updatePart\":[{\"attribute\":\"nickname\",\"cellInfo\":\"newLiXiaoLu\",\"userId\":\"user180108194823qevo\"}],\"addPart\":[]}");
    List updatePart=(List)jsonObject.get("updatePart");
    userSqlHandler.updateUsersId(updatePart);
    rs = statement.executeQuery("select nickname from users where userId = 'user180108194823qevo'");
    while (rs.next()) {
        str = rs.getString("nickname");
    }
    assertEquals("newLiXiaoLu", str);
    statement.executeUpdate("update users set nickname = 'LiXiaoLu' where userId = 'user180108194823qevo'");
} 

/** 
* 
* Method: isManager(Object object) 
* 
*/ 
@Test
public void testIsManager() throws Exception { 
//TODO: Test goes here...
    assertTrue(userSqlHandler.isManager(userManager));
} 


} 
