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
            // 1. ע�����ݿ������
            Class.forName("com.mysql.jdbc.Driver");

            // 2.ͨ��DriverManager��ȡ���ݿ�����
            String url = "jdbc:mysql://localhost:3306/student?uerUnicode=true&characterEncoding=utf8";
            String username = "root";
            String password = "123456";

            conn = DriverManager.getConnection(url, username, password);

            // 3.ͨ��Connection�����ȡStatement����
            stmt = conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeAll() {
        // 6.�������ݿ���Դ
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
            System.out.println("��ӭʹ��ѧ������ϵͳ��");
            System.out.println("��ѡ����ִ�еĲ�����1.��ѯ---2.����---3.�޸�---4.ɾ��---5.�˳�ϵͳ");
            int num = sc.nextInt();//����һ��1-5�����ֽ��й���ѡ��
            switch (num) {
                case 1:
                    System.out.println("��ӭ���в�ѯ��");
                    chaxun();
                    break;
                case 2:
                    System.out.println("��ӭ���в��룺");
                    charu();
                    break;
                case 3:
                    System.out.println("��ӭ�����޸ģ�");
                    xiugai();
                    break;
                case 4:
                    System.out.println("��ӭ����ɾ����");
                    shanchu();
                    break;
                case 5:
                    //System.exit(0); ֱ���˳�ϵͳ��Java�����ֹͣ
                    //�������ֱ���������ֹͣ����ô���ǿ����޸�f
                    f = false;
                    System.out.println("ѧ������ϵͳʹ�ý�����ллʹ�ã�");
            }

        }


    }

    public static void chaxun() {
        //����conn()������ʵ�����ݿ�123��
        conn();
        try {
            // 4.ʹ��Statementִ��SQL��䡣
            String sql = "select * from info";
            //(1)ֱ�ӵ���executeQuery(sql)��ɲ�ѯ���һ�ò�ѯ���
            rs = stmt.executeQuery(sql);
            // 5. ����ResultSet�����
            System.out.println("id \t\t username \t\t sex \t\t age");

            while (rs.next()) {
                int id = rs.getInt("id"); // ͨ��������ȡָ���ֶε�ֵ
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
            //ͨ�������������������Ա������
            System.out.println("������һ��ѧ�����������Ա�����䣺");
            String name = s.next();
            String sex = s.next();
            int age = s.nextInt();

            //�ڶ��ֲ��뷽��
            //ʹ��PreparedStatement�ӿ���ִ��SQL���
            String sql2 = "insert into info(username,sex,age)values(?,?,?)";
            pstmt = conn.prepareStatement(sql2);
            pstmt.setString(1, name);
            pstmt.setString(2, sex);
            pstmt.setInt(3, age);
            int n = pstmt.executeUpdate();
            if (n != -1) {
                System.out.println("����ɹ���");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void xiugai() {
        conn();
        try {
            Scanner s = new Scanner(System.in);
            //����switch  case ѡ��
            boolean f = true;
            while (f) {
                String sql;
                System.out.println("��ѡ�����޸ĵķ�ʽ��");
                System.out.println("1.�������������޸�����");
                System.out.println("2.����id�Ž����޸�����");
                System.out.println("3.�˳��޸Ĳ���");
                int num = s.nextInt();

                switch (num) {
                    case 1:
                        System.out.println("�����û�����");
                        String name = s.next();
                        System.out.println("�����޸ĵ�����");
                        int age = s.nextInt();
                        sql = "update info set age='" + age + "'where username='" + name + "'";
                        stmt.executeUpdate(sql);
                        break;


                    case 2:
                        System.out.println("�����û�id");
                        int id = s.nextInt();
                        System.out.println("�����޸ĵ�����");
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
            System.out.println("������Ҫɾ�����û�����");
            String name = s.next();
            String sql = "delete from info where username='" + name + "'";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
