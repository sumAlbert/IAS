package sql;

import org.junit.runner.Result;
import question.Question;
import user.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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
        boolean result=false;
        Question question=(Question) object;
        Connection connection=baseConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            String sqlStr = "insert into questions values (\"" + question.getQuestionId() + "\",\"" + question.getQuestionInfo() + "\",\"" + question.getQuestionType() + "\",\"" + question.getAnswerA() + "\",\""+ question.getAnswerB()+"\",\""+ question.getAnswerC()+"\",\""+ question.getAnswerD()+"\",\""+ question.getAnswerRight()+"\")";
            statement.execute(sqlStr);
            result=true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
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
                question.setQuestionType(resultSet.getString("questionType"));
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

    /**
     * 获取问题列表
     * @return
     */
    public List selectAllSQL(){
        List questions=new ArrayList();
        Connection connection=baseConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            String sqlStr = "select * from questions";
            ResultSet resultSet = statement.executeQuery(sqlStr);
            while (resultSet.next()){
                Question question=new Question();
                String questionId=resultSet.getString("questionId");
                String questionInfo=resultSet.getString("questionInfo");
                String questionType=resultSet.getString("questionType");
                String answerA=resultSet.getString("answerA");
                String answerB=resultSet.getString("answerB");
                String answerC=resultSet.getString("answerC");
                String answerD=resultSet.getString("answerD");
                String answerRight=resultSet.getString("answerRight");
                question.setQuestionInfo(questionInfo);
                question.setQuestionType(questionType);
                question.setQuestionId(questionId);
                question.setAnswerA(answerA);
                question.setAnswerB(answerB);
                question.setAnswerC(answerC);
                question.setAnswerD(answerD);
                question.setAnswerRight(answerRight);
                questions.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    /**
     * 根据questionId列表删除问题
     * @param questionsId 问题id列表
     */
    public void deleteUsersId(List questionsId){
        Connection connection=baseConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            for(int i=0;i<questionsId.size();i++){
                String questionId=(String)questionsId.get(i);
                String sqlStr = "delete from questions where questionId = '"+questionId+"'";
                statement.execute(sqlStr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据userId修改用户数据
     * @param users 用户数据列表
     */
    public void updateQuestionsId(List users){
        Connection connection=baseConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            for(int i=0;i<users.size();i++){
                Map user=(Map)users.get(i);
                String attribute=(String)user.get("attribute");
                String cellInfo=(String)user.get("cellInfo");
                String questionId=(String)user.get("userId");
                String sqlStr = "update questions set "+attribute+" = '"+cellInfo+"' where questionId = '"+questionId+"'";
                System.out.println(sqlStr);
                statement.execute(sqlStr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
