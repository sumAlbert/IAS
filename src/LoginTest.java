import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;
import org.apache.cactus.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
* Login Tester. 
* 
* @author <Authors name> 
* @since <pre>十二月 28, 2017</pre> 
* @version 1.0 
*/ 
public class LoginTest extends ServletTestCase{
    public LoginTest(String name) {
        super(name);
    }

    public static Test suite() {
        return (Test) new TestSuite(LoginTest.class);
    }

    public void testLogin(WebRequest webRequest,WebResponse webResponse) {
        webRequest.addParameter("account", "LiXiaoLu");
        webRequest.addParameter("pw", "lxl123");

    }

    public void testDoPost() {
        Login login = new Login();
        try {
            login.doPost(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
} 
