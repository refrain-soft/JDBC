package util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author shkstart
 * @create 2022-05-25-13:18
 */
/*
   操作数据库的工具类
 */
public class JDBCUtils {

    /*
    获取数据库的连接
     */
    public static Connection getConnection() throws Exception {
        //1.读取配置文件中4个基本信息
        //系统类加载器
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();

        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //2..加载驱动
        Class.forName(driverClass);

        //3. 获取连接
        Connection connection = DriverManager.getConnection(url, user, password);

        return connection;
    }

    /*
    关闭连接和Statement的操作
     */
    public static void closeResource(Connection connection, Statement ps) {
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //关闭资源
    public static void closeResource(Connection connection, Statement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
