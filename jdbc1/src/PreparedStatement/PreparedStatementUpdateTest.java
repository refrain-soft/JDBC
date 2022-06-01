package PreparedStatement;

import Util.JDBCMethod;
import Util.JDBCUtils;
import connection.ConnectionTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author shkstart
 * @create 2022-05-25-12:42
 */

/*
   使用PreparedStatement来替换Statement,实现对数据表的增删改查操作
   增: insert
   删: delete
   改: update

   查: select
 */
public class PreparedStatementUpdateTest {

    //向test01表中添加一条记录
    @Test
    public void testInsert(){
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            //1.读取配置文件中4个基本信息
            //系统类加载器
            // 输入流           类加载器      获取系统类加载器         获取资源作为流
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
            connection = DriverManager.getConnection(url, user, password);

//        System.out.println(connection);

            //4.预编译sql语句,返回PreparedStatement实例
            String sql = "insert into user_table(user,password,balance,birth)values(?,?,?,?)";//?: 占位符
            ps = connection.prepareStatement(sql);

            //5. 填充占位符
            ps.setString(1,"代广");
            ps.setString(2,"111111");
            ps.setString(3,"1000");
//        //出生日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = sdf.parse("2000-11-11");
        ps.setDate(4,new java.sql.Date(date.getTime()));

            //6. 执行sql操作
            ps.execute();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //7. 资源关闭
            try {
                if (ps != null)
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (connection != null)
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /*
    修改user_table表中的一条数据
     */
    @Test
    public void testUpdate() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //1. 获取数据库的连接
            connection = JDBCUtils.getConnection();

            //2. 预编译sql语句, 返回PreparedStatement的实例
            String sql = "update user_table set user = ? where balance = ?";
            ps = connection.prepareStatement(sql);

            //3. 填充占位符
            ps.setString(1,"邓林");
            ps.setString(2,"1000");

            //4. 执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5. 资源关闭
            JDBCUtils.closeResource(connection,ps);
        }
    }


    //删除表中字段
    @Test
    public void testJDBCMethodUpdate1(){
        String sql = "delete from user_table where user= ?";
        JDBCMethod.update(sql,"AA");
    }

    //换一个表修改表中数据
    @Test
    public void testJDBCMethodUpdate2(){
        String sql = "update `student` set grade=? where id=?";
        JDBCMethod.update(sql,"80","2");
    }
}
