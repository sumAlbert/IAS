package sql;

import user.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 98267
 */
public class UserSqlHandler implements SqlHandler {
    private BaseConnection baseConnection;

    public UserSqlHandler(BaseConnection baseConnection){
        this.baseConnection=baseConnection;
    }

    @Override
    public boolean insertSQL(Object object) {
        boolean result=false;
        User user=(User) object;
        Connection connection=baseConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            String sqlStr = "insert into users values (\"" + user.getUserId() + "\",\"" + user.getNickname() + "\",\"" + user.getUserAccount() + "\",\"" + user.getPw() + "\",0)";
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
        boolean result=false;
        User user=(User) object;
        Connection connection=baseConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            String sqlStr = "select userId,nickname from users where userAccount = \""+user.getUserAccount()+"\" and pw = \""+user.getPw()+"\"";
            ResultSet resultSet = statement.executeQuery(sqlStr);
            while (resultSet.next()){
                String nickname=resultSet.getString("nickname");
                String userId=resultSet.getString("userId");
                user.setUserId(userId);
                user.setNickname(nickname);
                result=true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取用户列表
     * @return
     */
    public List selectAllSQL(){
        List users=new ArrayList();
        Connection connection=baseConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            String sqlStr = "select * from users where userType = 0";
            ResultSet resultSet = statement.executeQuery(sqlStr);
            while (resultSet.next()){
                User user=new User();
                String nickname=resultSet.getString("nickname");
                String userId=resultSet.getString("userId");
                String userAccount=resultSet.getString("userAccount");
                String userPw=resultSet.getString("pw");
                user.setUserId(userId);
                user.setNickname(nickname);
                user.setUserAccount(userAccount);
                user.setPw(userPw);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * 根据userId列表删除玩家
     * @param usersId 用户id列表
     */
    public void deleteUsersId(List usersId){
        Connection connection=baseConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            for(int i=0;i<usersId.size();i++){
                String userId=(String)usersId.get(i);
                String sqlStr = "delete from users where userId = '"+userId+"'";
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
    public void updateUsersId(List users){
        Connection connection=baseConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            for(int i=0;i<users.size();i++){
                Map user=(Map)users.get(i);
                String attribute=(String)user.get("attribute");
                String cellInfo=(String)user.get("cellInfo");
                String userId=(String)user.get("userId");
                String sqlStr = "update users set "+attribute+" = '"+cellInfo+"' where userId = '"+userId+"'";
                System.out.println(sqlStr);
                statement.execute(sqlStr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否是管理员
     * @param object
     */
    public boolean isManager(Object object){
        boolean result=false;
        User user=(User) object;
        Connection connection=baseConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            String sqlStr = "select userType from users where userAccount = \""+user.getUserAccount()+"\" and pw = \""+user.getPw()+"\"";
            ResultSet resultSet = statement.executeQuery(sqlStr);
            while (resultSet.next()){
                int userType=resultSet.getInt("userType");
                if(userType==1){
                    result=true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
