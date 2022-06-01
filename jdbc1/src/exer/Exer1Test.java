package exer;

import Util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * @author shkstart
 * @create 2022-05-26-17:08
 * 课后练习一
 */
public class Exer1Test {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("输入用户名");
        String user = sc.nextLine();
        System.out.println("输入密码");
        String password = sc.nextLine();
        System.out.println("输入账户余额");
        String balance = sc.nextLine();
        System.out.println("输入生日");
        String birthday = sc.nextLine();//默认'2000-11-11';隐式转换

        String sql = "insert into user_table(user,password,balance,birth) values(?,?,?,?)";
        int insertCount = update(sql, user, password, balance, birthday);

        if (insertCount > 0){

            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }

    }



    public static int update(String sql, Object... args) {

        //通用的增删改操作
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //1. 获取数据库的连接
            connection = JDBCUtils.getConnection();

            //2. 预编译sql语句.返回PreparedStatement的实例
            ps = connection.prepareStatement(sql);

            //3. 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //4. 执行
            /*
            ps.execute():
              如果执行的是查询操作,有返回结果,则此方法返回true
              如果执行的是增删改操作,没有返回结果.则此方法返回false
             */
            //方式一
//           return ps.execute();

            //方式二
           return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5. 资源的关闭
            JDBCUtils.closeResource(connection,ps);
        }
          return 0;
    }
}
