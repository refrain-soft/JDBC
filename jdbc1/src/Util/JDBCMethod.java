package Util;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author shkstart
 * @create 2022-05-25-13:49
 *
 * 通用的增删改操作
 */
public class JDBCMethod {
    //sql中占位符的个数与可变形参的长度相同
    public static void update(String sql,Object ...args) {//不确定占位符个数和数据类型; 使用Object类型的可变形参代替
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //1. 获取数据库的连接
            connection = JDBCUtils.getConnection();

            //2. 预编译sql语句, 返回PreparedStatement的实例
            ps = connection.prepareStatement(sql);

            //3. 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1,args[i]);//小心参数声明错误
            }

            //4. 执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5. 关闭资源
            JDBCUtils.closeResource(connection,ps);
        }
    }
}
