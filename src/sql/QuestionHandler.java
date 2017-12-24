package sql;

import org.junit.runner.Result;
import question.Question;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author 98267
 */
public class QuestionHandler implements SqlHandler{
    /**
     * 数据库中questionType的列表
     */
    private ArrayList<String> questionList;
    /**
     * 数据库中questionType的对应的问题数目
     */
    private HashMap<String,String> questionMap;
    private BaseConnection baseConnection;
    public QuestionHandler(BaseConnection baseConnection){
        this.baseConnection=baseConnection;
        updateQuestions();
    }

    @Override
    public boolean insertSQL(Object object) {
        return false;
    }

    @Override
    public boolean deleteSQL(Object object) {
        return false;
    }

    @Override
    public boolean updateSQL(Object object) {
        return false;
    }

    @Override
    public Object selectSQL(Object object) {
        //从问题类别中获取questionTypeId
        int questionTypeNum=(int)object;
        questionTypeNum=questionTypeNum%(questionList.size());
        String questionTypeId=(String)questionList.get(questionTypeNum);
        int questionTypeNumMax=Integer.valueOf((String)questionMap.get(questionTypeId));
        Random random = new Random();
        int questionIndex=random.nextInt(questionTypeNumMax);

        Connection connection=baseConnection.getConnection();
        Question question=null;
        try (Statement statement = connection.createStatement()){
            String questionTypeQuery="select * from questions where questionType = '"+questionTypeId+"' limit "+questionIndex+",1";
            System.out.println(questionTypeQuery);
            ResultSet resultSet=statement.executeQuery(questionTypeQuery);
            while (resultSet.next()){
                String questionInfo=resultSet.getString("questionInfo");
                String answerA=resultSet.getString("answerA");
                String answerB=resultSet.getString("answerB");
                String answerC=resultSet.getString("answerC");
                String answerD=resultSet.getString("answerD");
                String answerRight=resultSet.getString("answerRight");
                question=new Question(questionInfo,answerA,answerB,answerC,answerD,answerRight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return question;
    }

    private boolean updateQuestions(){
        boolean result=false;
        Connection connection=baseConnection.getConnection();
        questionList=new ArrayList();
        questionMap=new HashMap(16);
        try (Statement statement = connection.createStatement()){
            String questionTypeQuery="select questionTypeId,questionTypeNum from questiontype";
            ResultSet resultSet=statement.executeQuery(questionTypeQuery);
            while (resultSet.next()){
                String questionTypeId=resultSet.getString("questionTypeId");
                String questionTypeNum=resultSet.getString("questionTypeNum");
                questionList.add(questionTypeId);
                questionMap.put(questionTypeId,questionTypeNum);
            }
            result=true;
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    public int getTypeNum(){
        return questionList.size();
    }
}
