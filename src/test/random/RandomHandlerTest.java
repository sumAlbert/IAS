package test.random; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import random.RandomHandler;

import static org.junit.Assert.assertEquals;

/** 
* RandomHandler Tester. 
* 
* @author <Authors name> 
* @since <pre>一月 1, 2020</pre>
* @version 1.0 
*/ 
public class RandomHandlerTest {
    private RandomHandler rh = null;
    private String str;

@Before
public void before() throws Exception {
    rh = new RandomHandler();
    str = null;
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: getId(int length) 
* 
*/ 
@Test
public void testGetId() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getQuestion(int length) 
* 
*/ 
@Test
public void testGetQuestion() throws Exception { 
//TODO: Test goes here... 
} 


/** 
* 
* Method: getTimeInfo() 
* 
*/ 
@Test
public void the_return_string_length__of_getId_should_be_12_if_the_input_length_is_equal_or_below_12_and_the_former_10_bytes_should_be_the_time_accurating_to_minute() {
//TODO: Test goes here...
    str = rh.getId(12);
    assertEquals(12, str.length());
    assertEquals("1801102020",str.substring(0, 10));
}

@Test
public void the_return_string_length__of_getId_should_be_12_if_the_input_length_is_equal_to_12_and_the_former_10_bytes_should_be_the_time_accurating_to_minute() {
    //TODO: Test goes here...
    str = rh.getId(11);
    System.out.println(str);
    assertEquals("1801102020",str.substring(0, 10));
    assertEquals(12, str.length());
    System.out.println(str);
}

@Test
public void the_return_string_length__of_getId_should_be_the_input_length_if_the_input_length_is_above_12_and_the_former_10_bytes_should_be_the_time_accurating_to_minute() {
    //TODO: Test goes here...
    str = rh.getId(16);
    assertEquals("1801102020",str.substring(0, 10));
    assertEquals(16, str.length());
}


} 
