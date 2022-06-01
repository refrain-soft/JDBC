package DAO;

import Bean.Student;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author shkstart
 * @create 2022-05-29-19:39
 * 此接口用于规范针对于student表的常用操作
 */
public interface StudentDAO {

    //将stu对象添加到数据库
    void insert(Connection conn, Student stu);

    //根据指定的id删除表中的数据
    void deleteById(Connection conn, int id);

    //针对于内存中的stu对象修改数据表中的对象
    void update(Connection conn, Student stu);

    //针对指定的id,查询得到对应的Student对象
    Student getStudentById(Connection conn, int id);

    //查询表中的所有记录构成的集合
    List<Student> getAll(Connection conn);

    //返回数据表中的数据的条目数
    Long getCount(Connection conn);

    //查看学生表中最大生日
    Date getMaxBirth(Connection conn);
}
