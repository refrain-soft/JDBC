package PreparedStatement;

import Bean.Student;
import Util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @author shkstart
 * @create 2022-05-25-18:01
 * 针对于Student表的查询操作
 */
public class StudentForQuery {
    /*
    针对与表的字段名与类的属性名不相同的情况
    1. 必须声明sql时使用类的属性名来命名字段的别名
    2. 在使用ResultSetMetaDate时,需要使用getColumnLabel()来替换getColumnName()获取列的别名

      说明: 如果sql中没有给字段起别名,getColumnLabel()仍然可以获取到列名
     */
    @Test
    public void testStudentForQuery() {
        //当数据表的字段名与当前类中属性名不一样时,可以在下面sql语句中使用别名的方式来与类中属性名匹配
        String sql = "select * from Student where id = ?";
        Student student = studentForQuery(sql, 2);
        System.out.println(student);
    }

    //通用的查询: 针对与student表的查询

    @Test
    public Student studentForQuery(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();

            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //执行获取结果集
            rs = ps.executeQuery();

            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();

            //获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                Student stu = new Student();
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值: 通过ResultSet
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个列的列名: 通过ResultSetMetaData
                    //获取列的列名用的是getColumnName(); 不推荐使用
                    //获取列的别名用的是getColumnLabel(); 在没有取别名的情况下获取到的就是列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射将对象指定名columnName属性赋值为指定的值columnValue
                    Field field = Student.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);//保证field是私有的也可以访问
                    field.set(stu, columnValue);
                }
                return stu;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps, rs);
        }
        return null;
    }


    //普通的查询
    @Test
    public void testQuery1() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select id,name,grade from Student where id=?";
            ps = connection.prepareStatement(sql);

            ps.setObject(1, "1");

            rs = ps.executeQuery();

            if (rs.next()) {
                int id = (int) rs.getObject(1);
                String name = (String) rs.getObject(2);
                float grade = (float) rs.getObject(3);

                Student student = new Student(id, name, grade);
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps, rs);
        }
    }
}
