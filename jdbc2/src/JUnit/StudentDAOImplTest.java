package JUnit;

import Bean.Student;
import DAO.StudentDAOImpl;
import JDBCUtils.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * @author shkstart
 * @create 2022-05-29-23:43
 */
class StudentDAOImplTest {

    private StudentDAOImpl dao = new StudentDAOImpl();

    @Test
    void insert() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Student stu = new Student(1, "刘锴", new Date(1973764974L), 80);
            dao.insert(conn, stu);
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    @Test
    void deleteById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            dao.deleteById(conn, 5);

            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    @Test
    void update() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            Student stu = new Student(9, "刘凯", new Date(1031592465747L), 85);
            dao.update(conn, stu);


            System.out.println("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    @Test
    void getStudentById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection1();

            Student stu = dao.getStudentById(conn, 8);
            System.out.println(stu);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    @Test
    void getAll() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            List<Student> list = dao.getAll(conn);
            list.forEach(System.out::println);

            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    @Test
    void getCount() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            Long count = dao.getCount(conn);
            System.out.println("表中的记录数为: " + count);

            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    @Test
    void getMaxBirth() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            java.sql.Date maxBirth = dao.getMaxBirth(conn);
            System.out.println("最大的生日为: " + maxBirth);

            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }
}