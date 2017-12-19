

import net.sf.json.JSONObject;
import random.RandomHandler;
import sql.BaseConnection;
import sql.UserSqlHandler;
import user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author 98267
 */
public class Register extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //预加载资源包括数据库链接和日志
        BaseConnection baseConnection=new BaseConnection("ias");
        Logger logger=Logger.getLogger("RegisterServlet");
        UserSqlHandler userSqlHandler=new UserSqlHandler(baseConnection);
        RandomHandler randomHandler=new RandomHandler();

        //获取ajax传递的参数
        String account=req.getParameter("account");
        String pw=req.getParameter("pw");
        String name=req.getParameter("name");

        //登录逻辑
        User user=new User();
        user.setUserAccount(account);
        user.setPw(pw);
        user.setNickname(name);

        boolean result= false;
        //判断注册的信息是否已经存在
        if(!(boolean)userSqlHandler.selectSQL(user)){
            user.setUserId("user"+randomHandler.getId(16));
            result=userSqlHandler.insertSQL(user);
            logger.info(String.valueOf(result));
            HttpSession httpSession=req.getSession();
            httpSession.setAttribute("user",user);
            result=true;
        }

        //将ajax需要的信息返回
        Map map=new HashMap(16);
        map.put("result",result);
        JSONObject jsonObject=JSONObject.fromObject(map);
        String jsonStr=jsonObject.toString();
        PrintWriter printWriter=resp.getWriter();
        printWriter.print(jsonStr);
        printWriter.close();

        //关闭预加载的资源
        baseConnection.closeConnection();
    }
}
