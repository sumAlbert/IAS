package user;

import random.RandomHandler;

import java.util.Calendar;

/**
 * @author 98267
 */
public class User {
    private String userId="";
    private String nickname="";
    private String userAccount="";
    private String pw="";
    public static final int USER_ID_LENGTH=20;


    public User(String nickname,String userAccount,String pw){
        super();
        this.nickname=nickname;
        this.userAccount=userAccount;
        this.pw=pw;
    }
    public User(){
        super();
    }

    public String getNickname() {
        return nickname;
    }
    public String getPw() {
        return pw;
    }
    public String getUserAccount() {
        return userAccount;
    }
    public String getUserId() {
        return userId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
