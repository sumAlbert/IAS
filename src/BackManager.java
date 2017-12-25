import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sql.BaseConnection;
import sql.QuestionHandler;
import sql.UserSqlHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author 98267
 */
public class BackManager extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Logger logger=Logger.getLogger("BackManager");
        BaseConnection baseConnection=new BaseConnection("ias");
        String command=req.getParameter("command");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("contentType","text/html;charset=utf-8");
        PrintWriter printWriter=resp.getWriter();
        switch (command){
            case "selectUser":{
                UserSqlHandler userSqlHandler=new UserSqlHandler(baseConnection);
                List users=userSqlHandler.selectAllSQL();
                Map resultBackMap=new HashMap(16);
                resultBackMap.put("users",users);
                resultBackMap.put("command","usersInfo");
                JSONObject jsonObject=JSONObject.fromObject(resultBackMap);
                logger.info(jsonObject.toString());
                printWriter.print(jsonObject.toString());
                break;
            }
            case "selectQuestion":{
                QuestionHandler questionHandler=new QuestionHandler(baseConnection);
                List question=questionHandler.selectAllSQL();
                Map resultBackMap=new HashMap(16);
                resultBackMap.put("questions",question);
                resultBackMap.put("command","selectQuestion");
                JSONObject jsonObject=JSONObject.fromObject(resultBackMap);
                logger.info(jsonObject.toString());
                printWriter.print(jsonObject.toString());
                break;
            }
            default:
                break;
        }
        baseConnection.closeConnection();
        printWriter.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
