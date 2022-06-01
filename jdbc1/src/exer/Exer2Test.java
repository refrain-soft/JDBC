package exer;

import Util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @author shkstart
 * @create 2022-05-26-17:46
 */
public class Exer2Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("选择你要进行的操作: ");
        System.out.println("1. 向表中添加数据");
        System.out.println("2. 通过身份证号或准考证号查询学生信息");
        System.out.println("3. 通过准考证号删除学生信息");
        System.out.println("4. 退出");
        int i = sc.nextInt();
        while (true) {
            switch (i) {
                case 1 -> testUpdate();
                case 2 -> queryWithIDCardORExamCard();
                case 3 -> testDeleteByExamCard();
                case 4 -> System.exit(0);
            }
        }
    }

    //向examstudent表中添加一条数据
    public static void testUpdate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("四级/六级: ");
        int Type = sc.nextInt();
        System.out.println("请输入身份证号");
        String IDCard = sc.next();
        System.out.println("请输入准考证号");
        String ExamCard = sc.next();
        System.out.println("请输入姓名");
        String StudentName = sc.next();
        System.out.println("请输入所在区域");
        String Location = sc.next();
        System.out.println("请输入成绩");
        int Grade = sc.nextInt();

        String sql = "insert into examstudent(Type,IDCard,ExamCard,StudentName,Location,Grade) values(?,?,?,?,?,?)";
        int update = update(sql, Type, IDCard, ExamCard, StudentName, Location, Grade);

        if (update > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }
    }

    //向表中添加数据
    public static int update(String sql, Object... args) {

        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();

            ps = connection.prepareStatement(sql);


            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }
        return 0;
    }


    public static void queryWithIDCardORExamCard() {
        System.out.println("请选择你要输入的类型");
        System.out.println("a: 准考证号");
        System.out.println("b: 身份证号");
        Scanner sc = new Scanner(System.in);
        String selection = sc.next();
        if ("a".equalsIgnoreCase(selection)) {//将变量值写在后面,避免空指针异常
            System.out.println("请输入准考证号:");
            String examCard = sc.next();

            String sql = "select FlowID,Type,IDCard,ExamCard,StudentName name,Location,Grade from examstudent where ExamCard=?";
            Student student = getInstance(Student.class, sql, examCard);

            if (student != null) {
                System.out.println(student);
            } else {
                System.out.println("输入的准考证号有误");
            }

        } else if ("b".equalsIgnoreCase(selection)) {
            System.out.println("请输入身份证号:");
            String IDCard = sc.next();

            String sql1 = "select FlowID,Type,IDCard,ExamCard,StudentName name,Location,Grade from examstudent where IDCard=?";
            Student student = getInstance(Student.class, sql1, IDCard);

            if (student != null) {
                System.out.println(student);
            } else {
                System.out.println("输入的身份证号有误");
            }
        } else {
            System.out.println("你的输入有误,请重新进入程序");
        }
    }


    /**
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     * @Description 问题二: 根据身份证号或者准考证号查询学生成绩
     */
    public static <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();

            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();

            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();//getColumnCount():获取列数

            if (rs.next()) {
                T t = clazz.getDeclaredConstructor().newInstance();
                //处理一行结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个类的类名
                    String columnLabel = rsmd.getColumnLabel(i + 1);//getColumnLabel():获取列标签

                    //给t这个对象指定的columnName属性,赋值为columnValue: 通过反射
                    Field field = clazz.getDeclaredField(columnLabel);//getDeclaredField():获取声明字段
                    field.setAccessible(true);
                    field.set(t, columnValue);

                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps, rs);
        }
        return null;
    }


    //问题三: 根据准考证号删除指定的学生信息
    public static void testDeleteByExamCard() {
        System.out.println("请输入准考证号");
        Scanner sc = new Scanner(System.in);
        String examCard = sc.next();

        //查询指定准考证号的学生
        String sql = "select FlowID,Type,IDCard,ExamCard,StudentName name,Location,Grade from " +
                "examstudent where ExamCard=?";

        Student student = getInstance(Student.class, sql, examCard);
        if (student == null) {
            System.out.println("查无此人请重新输入");
        } else {
            String sql1 = "delete from examstudent where ExamCard=?";
            int deleteCount = update(sql1, examCard);
            if (deleteCount > 0) {
                System.out.println("删除成功");
            } else {
                System.out.println("删除失败");
            }
        }
    }

    //优化
    public static void testDeleteByExamCard1() {
        System.out.println("请输入准考证号");
        Scanner sc = new Scanner(System.in);
        String examCard = sc.next();

        String sql = "delete from examstudent where ExamCard=?";
        int deleteCount = update(sql, examCard);
        if (deleteCount > 0) {
            System.out.println("删除成功");
        } else {
            System.out.println("查无此人请重新输入");
        }
    }
}
