package test;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
* Login Tester. 
* 
* @author <Authors name> 
* @since <pre>十二月 28, 2017</pre> 
* @version 1.0 
*/ 
public class LoginTest extends ServletTestCase{

    @Before
public void beginValidUser(WebRequest webRequest) {
        webRequest.addParameter("account","ll@qq.com");
        webRequest.addParameter("pw","123456");
    }

@After
public void after() throws Exception {
} 

/** 
* 
* Method: doGet(HttpServletRequest req, HttpServletResponse resp) 
* 
*/ 
@Test
public void testValidUser() throws Exception {
//TODO: Test goes here...

} 

/** 
* 
* Method: doPost(HttpServletRequest req, HttpServletResponse resp) 
* 
*/ 
@Test
public void testDoPost() throws Exception { 
//TODO: Test goes here... 
} 


} 
