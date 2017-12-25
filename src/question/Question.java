package question;

/**
 * @author 98267
 */
public class Question {
   private String questionInfo;
   private String answerA;
   private String answerB;
   private String answerC;
   private String answerD;
   private String answerRight;
   private String questionType;
   private String questionId;

   public Question(){
       super();
   }
   public Question(String questionInfo,String answerA,String answerB,String answerC,String answerD,String answerRight){
       super();
       this.answerA=answerA;
       this.answerB=answerB;
       this.answerC=answerC;
       this.answerD=answerD;
       this.answerRight=answerRight;
       this.questionInfo=questionInfo;
   }

    public String getAnswerA() {
        return answerA;
    }
    public String getAnswerB() {
        return answerB;
    }
    public String getAnswerC() {
        return answerC;
    }
    public String getAnswerD() {
        return answerD;
    }
    public String getAnswerRight() {
        return answerRight;
    }
    public String getQuestionInfo() {
        return questionInfo;
    }
    public String getQuestionType() {
        return questionType;
    }
    public String getQuestionId() {
        return questionId;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }
    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }
    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }
    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }
    public void setAnswerRight(String answerRight) {
        this.answerRight = answerRight;
    }
    public void setQuestionInfo(String questionInfo) {
        this.questionInfo = questionInfo;
    }
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
