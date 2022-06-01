package statementCrud;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

/**
 * @author shkstart
 * @create 2022-05-25-9:14
 */
public class StatementTest {
    //使用Statement的弊端: 需要拼写sql语句,并且存在在SQL注入的问题
    @Test
    public void testLogin(){
        Scanner sc = new Scanner(System.in);

        System.out.println("用户名: ");
        String user = sc.nextLine();
        System.out.println("密码");
        String password = sc.nextLine();

        String sql = "select user,password from user_table where user='"+ user +"' and password='"+ password +"'";
        User returnUser = get(sql, User.class);
        if (returnUser != null){
            System.out.println("登陆成功");
        } else {
            System.out.println("用户名不存在或密码错误");
        }

    }

    //使用Statement实现对数据表的查询操作
    private <T> T get(String sql, Class<T> clazz) {

        return null;
    }

    public static void main(String[] args) {
        StatementTest statementTest = new StatementTest();

        statementTest.testLogin();
    }
}
