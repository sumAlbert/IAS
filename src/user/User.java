package user;

import random.RandomHandler;

import java.util.Calendar;

/**
 * @author 98267
 */
public class User {

    public static final int CURRENTPAGE_LOBBY=0;
    public static final int CURRENTPAGE_PANEL=1;
    private String userId="";
    private String nickname="";
    private String userAccount="";
    private String pw="";

    private int tableId=0;
    private int positionId=0;
    private int playGameCurrentPage=CURRENTPAGE_LOBBY;
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
    public int getPlayGameCurrentPage() {
        return playGameCurrentPage;
    }
    public int getTableId() {
        return tableId;
    }
    public int getPositionId() {
        return positionId;
    }
    public void setPositionId(int positionId) {
        this.positionId = positionId;
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
    public void setPlayGameCurrentPage(int playGameCurrentPage) {
        this.playGameCurrentPage = playGameCurrentPage;
    }
    public void setTableId(int tableId) {
        this.tableId = tableId;
    }
}
