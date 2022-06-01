package DAO2;

import Bean.Student;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author shkstart
 * @create 2022-05-29-23:22
 */
public class StudentDAOImpl extends BaseDAO<Student> implements StudentDAO {


    @Override
    public void insert(Connection conn, Student stu) {
        String sql = "insert into student(name,birth,grade) values(?,?,?)";
        update(conn, sql, stu.getName(), stu.getBirth(), stu.getGrade());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql = "delete from student where id = ?";
        update(conn, sql, id);
    }

    @Override
    public void update(Connection conn, Student stu) {
        String sql = "update student set name=?,birth=?,grade=? where id = ?";
        update(conn, sql, stu.getName(), stu.getBirth(), stu.getGrade(), stu.getId());
    }

    @Override
    public Student getStudentById(Connection conn, int id) {
        String sql = "select id,name,birth,grade from student where id=?";
        Student student = getInstance(conn, sql, id);
        return student;
    }

    @Override
    public List<Student> getAll(Connection conn) {
        String sql = "select id,name,birth,grade from student";
        List<Student> list = getForList(conn, sql);
        return list;
    }

    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(*) from student";
        return getValue(conn, sql);

    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql = "select max(birth) from student";
        return getValue(conn, sql);
    }
}
