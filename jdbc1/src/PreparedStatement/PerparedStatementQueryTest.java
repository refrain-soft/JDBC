package PreparedStatement;

import Bean.Student;
import Bean.user_table;
import Util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shkstart
 * @create 2022-05-25-23:29
 * 使用PerparedStatement实现针对于不同表的通用的查询操作
 */
public class PerparedStatementQueryTest {

    @Test
    public void testGetFroList(){
        String sql = "select * from student where id < ?";
        List<Student> list = getForList(Student.class, sql, 5);
        list.forEach(System.out::println);

        System.out.println();

        String sql1 = "select * from user_table where balance =?";
        List<user_table> forList = getForList(user_table.class, sql1, 10000);
        forList.forEach(System.out::println);

        System.out.println();

        //查询一个表中的所有数据
        String sql2 = "select * from user_table";
        List<user_table> forList1 = getForList(user_table.class, sql2);
        forList1.forEach(System.out::println);


    }

    /**
     * @Description 针对于不同的表的通用查询操作,返回表中的多条记录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> List<T> getForList(Class<T> clazz, String sql, Object ... args){
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

            //创建集合对象
            ArrayList<T> list = new ArrayList<>();

            while (rs.next()){
                T t = clazz.getDeclaredConstructor().newInstance();
                //处理一行结果集一行数据中的每一个列: 给t对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个类的类名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给t这个对象指定的columnName属性,赋值为columnValue: 通过反射
                    Field field =  clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);

                }
                list.add(t);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,ps,rs);
        }
        return null;
    }


    @Test
    public void testGEtInstance(){
        String sql = "select * from user_table where user=?";
        user_table aa = getInstance(user_table.class, sql, "邓林");
        System.out.println(aa);

        String sql1 = "select * from student where id=?";
        Student student = getInstance(Student.class, sql1, 1);
        System.out.println(student);
    }


    /**
     * @Description 针对于不同的表的通用查询操作,返回表中的一条记录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> clazz,String sql,Object ... args){
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
            int columnCount = rsmd.getColumnCount();//getColumnCount():获取列数

            if (rs.next()){
                T t = clazz.getDeclaredConstructor().newInstance();
                //处理一行结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个类的类名
                    String columnLabel = rsmd.getColumnLabel(i + 1);//getColumnLabel():获取列标签

                    //给t这个对象指定的columnName属性,赋值为columnValue: 通过反射
                    Field field =  clazz.getDeclaredField(columnLabel);//getDeclaredField():获取声明字段
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


    //问题二: 根据身份证号或者准考证号查询学生成绩

}
