package sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 获取数据库连接
 * @author 98267
 */
public class BaseConnection{
    /**
     * 数据库配置信息
     */
    private final static String USER="root";
    private final static String PASS="123456";
    private final static String DB_URL_PRE="jdbc:mysql://localhost:3306/";
    private final static String DB_URL_AFT="?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    private Connection connection;

    public BaseConnection(String database){
        try{
            String dbURL=DB_URL_PRE+database+DB_URL_AFT;
            Driver driver=new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(driver);
            connection=DriverManager.getConnection(dbURL,USER,PASS);
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}