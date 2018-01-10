import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import question.Question;
import random.RandomHandler;
import sql.BaseConnection;
import sql.QuestionHandler;
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
        req.setCharacterEncoding("UTF-8");
        BaseConnection baseConnection=new BaseConnection("ias");
        String command=req.getParameter("command");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("contentType","text/html;charset=utf-8");
        PrintWriter printWriter=resp.getWriter();
        switch (command){
            case "verify":{
                UserSqlHandler userSqlHandler=new UserSqlHandler(baseConnection);
                HttpSession httpSession=req.getSession();
                User user=(User)httpSession.getAttribute("user");
                if(user==null){
                    printWriter.print(false);
                }
                else{
                    if(userSqlHandler.isManager(user)){
                        printWriter.print(true);
                    }
                    else {
                        printWriter.print(false);
                    }
                }
                break;
            }
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
            case "deleteUsers":{
                UserSqlHandler userSqlHandler=new UserSqlHandler(baseConnection);
                String data=req.getParameter("data");
                JSONObject jsonObject=JSONObject.fromObject(data);
                List deleteUsersId=(List)jsonObject.get("deleteItems");
                userSqlHandler.deleteUsersId(deleteUsersId);
                break;
            }
            case "deleteQuestion":{
                QuestionHandler questionHandler=new QuestionHandler(baseConnection);
                String data=req.getParameter("data");
                JSONObject jsonObject=JSONObject.fromObject(data);
                List deleteUsersId=(List)jsonObject.get("deleteItems");
                questionHandler.deleteUsersId(deleteUsersId);
                break;
            }
            case "saveUser":{
                UserSqlHandler userSqlHandler=new UserSqlHandler(baseConnection);
                String data=req.getParameter("data");
                RandomHandler randomHandler=new RandomHandler();
                JSONObject jsonObject=JSONObject.fromObject(data);

                //处理增加的元素
                List addPart=(List)jsonObject.get("addPart");
                if(addPart.size()!=0){
                    for(int i=0;i<addPart.size();i++){
                        User user=new User();
                        JSONArray jsonUser=(JSONArray)addPart.get(i);
                        user.setNickname((String)jsonUser.get(0));
                        user.setUserAccount((String)jsonUser.get(1));
                        user.setPw((String)jsonUser.get(2));
                        user.setUserId("user"+randomHandler.getId(16));
                        userSqlHandler.insertSQL(user);
                    }
                }

                //处理修改过的元素
                List updatePart=(List)jsonObject.get("updatePart");
                userSqlHandler.updateUsersId(updatePart);
                break;
            }
            case "saveQuestion":{
                QuestionHandler questionHandler=new QuestionHandler(baseConnection);
                String data=req.getParameter("data");
                RandomHandler randomHandler=new RandomHandler();
                JSONObject jsonObject=JSONObject.fromObject(data);
                //处理增加的元素
                List addPart=(List)jsonObject.get("addPart");
                if(addPart.size()!=0){
                    for(int i=0;i<addPart.size();i++){
                        Question question=new Question();
                        JSONArray jsonUser=(JSONArray)addPart.get(i);
                        question.setQuestionInfo((String)jsonUser.get(0));
                        question.setQuestionType((String)jsonUser.get(1));
                        question.setAnswerA((String)jsonUser.get(2));
                        question.setAnswerB((String)jsonUser.get(3));
                        question.setAnswerC((String)jsonUser.get(4));
                        question.setAnswerD((String)jsonUser.get(5));
                        question.setAnswerRight((String)jsonUser.get(6));
                        question.setQuestionId("question"+randomHandler.getId(12));
                        questionHandler.insertSQL(question);
                    }
                }

                //处理修改过的元素
                List updatePart=(List)jsonObject.get("updatePart");
                questionHandler.updateQuestionsId(updatePart);
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
