package utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author shkstart
 * @create 2022-05-31-0:18
 */
public class JDBCUtils {

    //使用DBCP数据库连接池技术获取数据库的连接

    private static DataSource source;

    static {
        try {
            Properties pros = new Properties();

            //方式一:
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");

            //方式二:
//            FileInputStream is = new FileInputStream(new File("dbcp.properties"));

            pros.load(is);

            //创建DBCP数据库连接池
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection() throws Exception {
        Connection conn = source.getConnection();

        return conn;
    }


    //使用Druid数据库连接池技术
    private static DataSource source1;

    static {
        try {
            Properties pros = new Properties();

            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");

            pros.load(is);

            source1 = DruidDataSourceFactory.createDataSource(pros);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection1() throws SQLException {
        Connection conn = source1.getConnection();

        return conn;
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
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 使用dbutils.jar中提供的Dbutils工具类,实现资源的关闭

    public static void closeResource1(Connection conn, Statement ps, ResultSet rs) {
        //方式一:
//        try {
//            DbUtils.close(rs);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(ps);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(conn);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        //方式二:
        DbUtils.closeQuietly(rs);
        DbUtils.closeQuietly(ps);
        DbUtils.closeQuietly(conn);
    }
}
