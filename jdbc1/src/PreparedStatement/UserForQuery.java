package PreparedStatement;

import Bean.user_table;
import Util.JDBCUtils;
import org.junit.jupiter.api.Test;
import statementCrud.User;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author shkstart
 * @create 2022-05-25-14:49
 * @Description 针对于User表的查询操作
 */
public class UserForQuery {

    @Test
    public void  testQueryForUser_table(){
        String sql = "select user,password,balance,birth from user_table where user=?";

        user_table user_table = queryForUser_table(sql, "邓林");
        System.out.println(user_table);

        sql = "select user,balance from user_table where user=?";
        Bean.user_table userName = queryForUser_table(sql, "邓林");
        System.out.println(userName);
    }

    /*
    针对与user_table这个表的通用查询操作
     */
    public user_table queryForUser_table(String sql, Object ... args){
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
                user_table ut = new user_table();
                //处理一行结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个类的类名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给ut这个对象指定的columnName属性,赋值为columnValue: 通过反射
                    Field field = user_table.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(ut,columnValue);

                }
                return ut;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,ps,rs);
        }
        return null;
    }

    @Test
    public void testQuery1() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();

            String sql = "select user,password,balance,birth from user_table where user = ?";
            ps = connection.prepareStatement(sql);

            //填充占位符
            ps.setObject(1,"邓林");

            //执行,并返回结果集
            resultSet = ps.executeQuery();

            //处理结果集
            if (resultSet.next()){//next():判断结果集的下一条是否有数据,如果有数据返回true,并指针下移;如果返回false,指针不下移;

                //获取当前这条数据的各个字段的值
                String user = resultSet.getString(1);
                String password = resultSet.getString(2);
                String balance = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                //方式一:
//                System.out.println("user = " + user + ",password = " + password+ ",balance = " + balance + ",birth = " + birth);

                //方式二:
//                Object[] data = new Object[]{user,password,balance,birth};

                //方式三: 将数据封装为一个对象(推荐)
                user_table ut = new user_table(user, password, balance, birth);
                System.out.println(ut);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(connection,ps,resultSet);
        }

    }
}
