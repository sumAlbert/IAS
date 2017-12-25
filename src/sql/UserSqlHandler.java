package sql;

import user.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
            String sqlStr = "insert into users values (\"" + user.getUserId() + "\",\"" + user.getNickname() + "\",\"" + user.getUserAccount() + "\",\"" + user.getPw() + "\")";
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
            String sqlStr = "select * from users";
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

}
