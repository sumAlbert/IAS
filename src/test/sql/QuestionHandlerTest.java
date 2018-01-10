package test.sql; 

import com.sun.org.apache.xerces.internal.xs.StringList;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import org.junit.runner.Result;
import question.Question;
import sql.BaseConnection;
import sql.QuestionHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/** 
* QuestionHandler Tester. 
* 
* @author <Authors name> 
* @since <pre>一月 2, 2018</pre> 
* @version 1.0 
*/ 
public class QuestionHandlerTest {
    private Question question1 = null;
    private Question question2 = null;
    private BaseConnection baseConnection = null;
    private Connection connection = null;
    private QuestionHandler questionHandler = null;
    private Statement statement = null;
@Before
public void before() throws Exception {
    question1 = new Question("晚上吃的是什么？", "", "", "", "", "0");
    question1.setQuestionId("4");
    question1.setQuestionType("0");
    question2 = new Question("早上吃的是什么？", "", "", "", "", "0");
    question2.setQuestionId("5");
    question2.setQuestionType("0");
    baseConnection = new BaseConnection("ias");
    questionHandler = new QuestionHandler(baseConnection);
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
    questionHandler.insertSQL(question1);
    boolean isInserted = false;
    connection = baseConnection.getConnection();
    statement = connection.createStatement();
    ResultSet rs = statement.executeQuery("select * from questions");
    while(rs.next()) {
        if(rs.getString("questionId").equals("4")) {
            isInserted = true;
        }
    }
    assertTrue(isInserted);
    statement.executeUpdate("delete from questions where questionId = '4'");
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
    Question question = (Question)questionHandler.selectSQL(0);
    assertTrue(question.getQuestionType().equals("0"));
} 

/** 
* 
* Method: getTypeNum() 
* 
*/ 
@Test
public void testGetTypeNum() throws Exception { 
//TODO: Test goes here...
    assertEquals(4, questionHandler.getTypeNum());
} 

/** 
* 
* Method: selectAllSQL() 
* 
*/ 
@Test
public void testSelectAllSQL() throws Exception { 
//TODO: Test goes here...
    List list = questionHandler.selectAllSQL();
    assertEquals(6, list.size());
} 

/** 
* 
* Method: deleteUsersId(List questionsId) 
* 
*/ 
@Test
public void testDeleteUsersId() throws Exception { 
//TODO: Test goes here...
    List deleteQues = new ArrayList<String>();
    deleteQues.add("4");
    deleteQues.add("5");
    questionHandler.insertSQL(question1);
    questionHandler.insertSQL(question2);
    boolean isDeleted = true;
    connection = baseConnection.getConnection();
    statement = connection.createStatement();
    questionHandler.deleteUsersId(deleteQues);
    ResultSet rs = statement.executeQuery("select * from questions");
    while(rs.next()) {
        if(rs.getString("questionId").equals("4") || rs.getString("questionId").equals("5")) {
            isDeleted = false;
        }
    }
    assertTrue(isDeleted);
    connection.close();

} 





} 
