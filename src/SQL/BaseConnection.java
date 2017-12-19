package SQL;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

/**
 * 获取数据库连接
 */
public class BaseConnection{
    //数据库配置信息
    private final String USER="root";
    private final String PASS="123aaaaaa";
    private final String DB_URL_PRE="jdbc:mysql://localhost:3306/";
    private final String DB_URL_AFT="?useUnicode=true&characterEncoding=UTF-8";
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
    public Connection getConnection(){
        return connection;
    }
}