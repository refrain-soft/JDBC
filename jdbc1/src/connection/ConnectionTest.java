package connection;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author shkstart
 * @create 2022-05-24-22:52
 */
public class ConnectionTest {
    //方式一:
    @Test
    public void testConnection1() throws SQLException {
        //1. 获取Driver的实现类对象
        Driver driver = new com.mysql.jdbc.Driver();

        //url: http://localhost:8080/gmall/keyboard.jpg

        //jdbc:mysql: 协议
        //localhost: ip地址
        //3306: 默认mysql端口号
        //test01: test01数据库(数据库名)
        String url = "jdbc:mysql://localhost:3306/test01";

        //将用户名和密码封装再Properties中
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","sy123456");


        Connection connect = driver.connect(url, info);

        System.out.println(connect);

    }

    //方式二: 对方式一的迭代:在如下的程序中不会出现第三方的api,使得程序具有更好的可移植性
    @Test
    public void testConnection2() throws Exception {
        //1. 获取Driver的实现类对象: 使用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver =(Driver) clazz.getDeclaredConstructor().newInstance();

        //2. 提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/test01";

        //3. 提供连接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","sy123456");

        //4. 获取连接
        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式三: 使用DriverManager替换Driver
    @Test
    public void testConnection3() throws Exception {
        //1. 获取Driver的实现类对象
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver =(Driver) clazz.getDeclaredConstructor().newInstance();

        //2. 提供另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test01";
        String user = "root";
        String password = "sy123456";

        //注册驱动
        DriverManager.registerDriver(driver);

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    //方式四: 可以只是加载驱动,不用显示的注册驱动了
    @Test
    public void testConnection4() throws Exception {
        //1. 提供另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test01";
        String user = "root";
        String password = "sy123456";

        //2. 加载Driver
        Class.forName("com.mysql.jdbc.Driver");//使用mysql可以省略这一步,其他的数据库不可以省略
//        相较于方式三,,可以省略如下的操作
//        Driver driver =(Driver) clazz.getDeclaredConstructor().newInstance();
//        //注册驱动
//        DriverManager.registerDriver(driver);
        //为什么可以省略?
        /*
        在MySQL的Driver实现类中,声明了如下的操作
     static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }
         */

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    //方式五(final版): 将数据库连接需要的4个 基本信息声明在配置文件中,通过读取配置文件的方式,获取连接\
    /*好处:
    1. 实现了数据于代码的分离,实现了解耦
    2. 如果需要修改配置文件信息,可以避免程序重新打包
     */

    @Test
    public void testConnection5() throws Exception {

        //1.读取配置文件中4个基本信息
        //系统类加载器
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();

        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");//获取驱动

        //2..加载驱动
        Class.forName(driverClass);

        //3. 获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

    }
}
