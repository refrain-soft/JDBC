package dbutils;

import bean.Student;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.jupiter.api.Test;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author shkstart
 * @create 2022-05-31-14:21
 */
/*
commons-dbutils 是Apache阻止提供的一个开源的JDBC工具类库,封装了针对于数据库的增删改查操作
 */
public class QueryRunnerTest {

    //测试插入
    @Test
    public void testInsert() {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();

            conn = JDBCUtils.getConnection1();

            String sql = "insert into student(name,birth,grade)values(?,?,?)";

            int insertCount = runner.update(conn, sql, "张洋", "2000-06-06", 90);

            System.out.println(insertCount);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    //测试查询: 测试5种不同的处理器
    /*
    BeanHandler: 是ResultSetHandler接口的实现类,用于封装表中的一条记录
     */

    @Test
    public void testQuery1() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();

            conn = JDBCUtils.getConnection1();

            String sql = "select id,name,birth,grade from student where id=?";

            BeanHandler<Student> handler = new BeanHandler<>(Student.class);

            Student student = runner.query(conn, sql, handler, 9);

            System.out.println(student);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /*
    BeanListHandler: 是ResultSetHandler接口的实现类,用于封装表中的多条记录构成的集合
     */
    @Test
    public void testQuery2() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();

            conn = JDBCUtils.getConnection1();

            String sql = "select id,name,birth,grade from student where id<?";

            BeanListHandler<Student> beanListHandler = new BeanListHandler<>(Student.class);

            List<Student> studentList = runner.query(conn, sql, beanListHandler, 9);

            studentList.forEach(System.out::println);
//            System.out.println(studentList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    //测试查询
    /*
    MapHandler: 是ResultSetHandler接口的实现类,对应表中的一条记录
                将字段及相应字段的值作为map中的key和value
     */

    @Test
    public void testQuery3() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();

            conn = JDBCUtils.getConnection1();

            String sql = "select id,name,birth,grade from student where id=?";

            //声明MapHandler
            MapHandler handler = new MapHandler();

            Map<String, Object> map = runner.query(conn, sql, handler, 9);

            System.out.println(map);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /*
BeanListHandler: 是ResultSetHandler接口的实现类,对应表中的多条记录构成的集合
                 将字段及相关的值作为map中的key和value;将这些map添加到List中
 */
    @Test
    public void testQuery4() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();

            conn = JDBCUtils.getConnection1();

            String sql = "select id,name,birth,grade from student where id<?";

            MapListHandler listHandler = new MapListHandler();

            List<Map<String, Object>> list = runner.query(conn, sql, listHandler, 9);

            list.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    //ScalarHandler: 查询特殊值
    @Test
    public void testQuery5() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();

            conn = JDBCUtils.getConnection1();

            String sql = "select count(*) from student";

            ScalarHandler<Object> handler = new ScalarHandler<>();

            Object count = runner.query(conn, sql, handler);

            System.out.println(count);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void testQuery6() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();

            conn = JDBCUtils.getConnection1();

            String sql = "select max(birth) from student";

            ScalarHandler<Object> handler = new ScalarHandler<>();

            Object date = runner.query(conn, sql, handler);

            System.out.println(date);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource1(conn, null, null);
        }
    }


    //自定义ResultSetHandler的实现类
    @Test
    public void testQuery7() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();

            conn = JDBCUtils.getConnection1();

            //查询表中的一条记录
            String sql = "select id,name,birth,grade from student where id = ?";

            ResultSetHandler<Student> handler = new ResultSetHandler<Student>() {
                @Override
                public Student handle(ResultSet resultSet) throws SQLException {
//                    return new Student(9, "tt", new Date(19734623974L), 80);

                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        Date birth = resultSet.getDate("birth");
                        int grade = resultSet.getInt("grade");

                        return new Student(id, name, birth, grade);
                    }
                    return null;
                }
            };


            Student student = runner.query(conn, sql, handler, 9);

            System.out.println(student);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }
}
