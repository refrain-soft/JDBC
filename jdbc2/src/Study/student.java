package Study;

import java.sql.*;
import java.util.Scanner;

class SGLXT {

    static Statement stmt = null;
    static ResultSet rs = null;
    static Connection conn = null;
    static PreparedStatement pstmt = null;


    public static void conn() {
        try {
            // 1. 注册数据库的驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 2.通过DriverManager获取数据库连接
            String url = "jdbc:mysql://localhost:3306/student?uerUnicode=true&characterEncoding=utf8";
            String username = "root";
            String password = "123456";

            conn = DriverManager.getConnection(url, username, password);

            // 3.通过Connection对象获取Statement对象
            stmt = conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeAll() {
        // 6.回收数据库资源
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            stmt = null;
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean f = true;

        while (f) {
            System.out.println("欢迎使用学生管理系统！");
            System.out.println("请选择你执行的操作：1.查询---2.插入---3.修改---4.删除---5.退出系统");
            int num = sc.nextInt();//输入一个1-5的数字进行功能选择
            switch (num) {
                case 1:
                    System.out.println("欢迎进行查询：");
                    chaxun();
                    break;
                case 2:
                    System.out.println("欢迎进行插入：");
                    charu();
                    break;
                case 3:
                    System.out.println("欢迎进行修改：");
                    xiugai();
                    break;
                case 4:
                    System.out.println("欢迎进行删除：");
                    shanchu();
                    break;
                case 5:
                    //System.exit(0); 直接退出系统，Java虚拟机停止
                    //如果不想直接让虚拟机停止，那么我们可以修改f
                    f = false;
                    System.out.println("学生管理系统使用结束，谢谢使用！");
            }

        }


    }

    public static void chaxun() {
        //调用conn()方法，实现数据库123步
        conn();
        try {
            // 4.使用Statement执行SQL语句。
            String sql = "select * from info";
            //(1)直接调用executeQuery(sql)完成查询并且获得查询结果
            rs = stmt.executeQuery(sql);
            // 5. 操作ResultSet结果集
            System.out.println("id \t\t username \t\t sex \t\t age");

            while (rs.next()) {
                int id = rs.getInt("id"); // 通过列名获取指定字段的值
                String name = rs.getString("username");
                String sex = rs.getString("sex");
                int age = rs.getInt("age");

                System.out.println(id + "\t\t" + name + "\t\t" + sex + "\t\t" + age);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void charu() {
        conn();
        try {
            Scanner s = new Scanner(System.in);
            //通过键盘输入获得姓名，性别和年龄
            System.out.println("请输入一个学生的姓名、性别和年龄：");
            String name = s.next();
            String sex = s.next();
            int age = s.nextInt();

            //第二种插入方法
            //使用PreparedStatement接口来执行SQL语句
            String sql2 = "insert into info(username,sex,age)values(?,?,?)";
            pstmt = conn.prepareStatement(sql2);
            pstmt.setString(1, name);
            pstmt.setString(2, sex);
            pstmt.setInt(3, age);
            int n = pstmt.executeUpdate();
            if (n != -1) {
                System.out.println("插入成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void xiugai() {
        conn();
        try {
            Scanner s = new Scanner(System.in);
            //增加switch  case 选项
            boolean f = true;
            while (f) {
                String sql;
                System.out.println("请选择你修改的方式：");
                System.out.println("1.根据姓名进行修改年龄");
                System.out.println("2.根据id号进行修改姓名");
                System.out.println("3.退出修改操作");
                int num = s.nextInt();

                switch (num) {
                    case 1:
                        System.out.println("输入用户姓名");
                        String name = s.next();
                        System.out.println("输入修改的年龄");
                        int age = s.nextInt();
                        sql = "update info set age='" + age + "'where username='" + name + "'";
                        stmt.executeUpdate(sql);
                        break;


                    case 2:
                        System.out.println("输入用户id");
                        int id = s.nextInt();
                        System.out.println("输入修改的姓名");
                        String name1 = s.next();
                        sql = "update info set username='" + name1 + "'where id='" + id + "'";
                        stmt.executeUpdate(sql);
                        break;
                    case 3:
                        f = false;
                        break;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void shanchu() {
        conn();
        Scanner s = new Scanner(System.in);
        try {
            System.out.println("输入需要删除的用户姓名");
            String name = s.next();
            String sql = "delete from info where username='" + name + "'";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
