package test.user; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import user.User;

import static org.junit.Assert.assertEquals;

/** 
* User Tester. 
* 
* @author <Authors name> 
* @since <pre>十二月 26, 2017</pre> 
* @version 1.0 
*/ 
public class UserTest {
    User user = null;
    User user1 = null;

@Before
public void before() throws Exception {
    user = new User("Daniel","LL1997","000000");
    user1 = new User();
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: getNickname() 
* 
*/ 
@Test
public void the_user_nickname_should_be_Daniel_after_initialize() throws Exception {
//TODO: Test goes here...
    assertEquals("Daniel", user.getNickname());
} 

/** 
* 
* Method: getPw() 
* 
*/ 
@Test
public void the_user_password_should_be_000000_after_initialize() throws Exception {
//TODO: Test goes here...
    assertEquals("000000", user.getPw());
} 

/** 
* 
* Method: getUserAccount() 
* 
*/ 
@Test
public void the_useraccount_should_be_LL1997_after_initialize() throws Exception {
//TODO: Test goes here...
    assertEquals("LL1997", user.getUserAccount());
} 

/** 
* 
* Method: getUserId() 
* 
*/ 
@Test
public void testGetUserId() throws Exception { 
//TODO: Test goes here...
    assertEquals("", user.getUserId());
} 

/** 
* 
* Method: getPlayGameCurrentPage() 
* 
*/ 
@Test
public void testGetPlayGameCurrentPage() throws Exception { 
//TODO: Test goes here...
    assertEquals(0, user.getPlayGameCurrentPage());
} 

/** 
* 
* Method: getTableId() 
* 
*/ 
@Test
public void testGetTableId() throws Exception { 
//TODO: Test goes here...
    assertEquals(0, user.getTableId());
} 

/** 
* 
* Method: getPositionId() 
* 
*/ 
@Test
public void testGetPositionId() throws Exception { 
//TODO: Test goes here...
    assertEquals(0, user.getPositionId());
} 

/** 
* 
* Method: setPositionId(int positionId) 
* 
*/ 
@Test
public void testSetPositionId() throws Exception { 
//TODO: Test goes here...
    user1.setPositionId(2);
    assertEquals(2, user1.getPositionId());
} 

/** 
* 
* Method: setNickname(String nickname) 
* 
*/ 
@Test
public void testSetNickname() throws Exception { 
//TODO: Test goes here...
    user1.setNickname("Tony");
    assertEquals("Tony", user1.getNickname());
} 

/** 
* 
* Method: setPw(String pw) 
* 
*/ 
@Test
public void testSetPw() throws Exception { 
//TODO: Test goes here...
    user1.setPw("111111");
    assertEquals("111111", user1.getPw());
} 

/** 
* 
* Method: setUserAccount(String userAccount) 
* 
*/ 
@Test
public void testSetUserAccount() throws Exception { 
//TODO: Test goes here...
    user1.setUserAccount("Tony123");
    assertEquals("Tony123", user1.getUserAccount());
} 

/** 
* 
* Method: setUserId(String userId) 
* 
*/ 
@Test
public void testSetUserId() throws Exception { 
//TODO: Test goes here...
    user1.setUserId("1");
    assertEquals("1", user1.getUserId());
} 

/** 
* 
* Method: setPlayGameCurrentPage(int playGameCurrentPage) 
* 
*/ 
@Test
public void testSetPlayGameCurrentPage() throws Exception { 
//TODO: Test goes here...
    user1.setPlayGameCurrentPage(2);
    assertEquals(2, user1.getPlayGameCurrentPage());
} 

/** 
* 
* Method: setTableId(int tableId) 
* 
*/ 
@Test
public void testSetTableId() throws Exception { 
//TODO: Test goes here...
    user1.setTableId(3);
    assertEquals(3, user1.getTableId());
} 


} 
