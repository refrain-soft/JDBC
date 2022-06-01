package statementCrud;

import Util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @Description 使用PreparedStatement来替换Statement,解决sql注入问题
 * @author shkstart
 * @create 2022-05-26-16:17
 *
 * 除了Statement的拼串,sql问题之外,PreparedStatement还有哪些好处?
 * 1. PreparedStatement操作Blob的数据,而Statement做不到
 * 2. PreparedStatement可以实现更高效的的批量操作
 */
public class PreparedStatementTest {


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("用户名: ");
        String user = sc.nextLine();
        System.out.println("密码");
        String password = sc.nextLine();

//        String sql = "select user,password from user_table where user='"+ user +"' and password='"+ password +"'";
        String sql = "select user,password from user_table where user=? and password=?";
        User returnUser = getInstance(User.class, sql,user,password);
        if (returnUser != null){
            System.out.println("登陆成功");
        } else {
            System.out.println("用户名不存在或密码错误");
        }

    }



    /**
     * @Description 针对于不同的表的通用查询操作,返回表中的一条记录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<T> clazz, String sql, Object... args){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();

            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1 ,args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()){
                T t = clazz.getDeclaredConstructor().newInstance();
                //处理一行结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个类的类名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给t这个对象指定的columnName属性,赋值为columnValue: 通过反射
                    Field field =  clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);

                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,ps,rs);
        }
        return null;
    }
}
