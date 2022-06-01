package blob;

import Util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author shkstart
 * @create 2022-05-27-12:59
 */
/*
 * 使用PreparedStatement实现批量数据的操作
 * update, delete本身就具有批量操作的效果
 * 此时的批量操作主要指的是批量插入;使用PreparedStatement实现批量插入
 *
 * 题目: 在数据库的goods表中插入20000条数据
 *  create table goods(
 *     id int primary key auto_increment,
 *     name varchar(25)
 *  );
 *
 * 方式一: Statement;
 * Connection coon = JDBCUtils.getConnection();
 * Statement st = coon.createStatement();
 * for(int i= 1;i<= 20000;i++){
 *    String sql = "insert into goods(name)values('name_'"+ i +")"//每次运行的sql都不一样
 *    st.execute(sql);
 * }
 */
public class InsertTest {

    //方式二: 使用PreparedStatement
    @Test
    public void testInsert1() {
        Connection coon = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            coon = JDBCUtils.getConnection();

            String sql = "insert into goods(name)values(?)";//预编译sql语句,只运行一次
            ps = coon.prepareStatement(sql);

            for (int i = 1; i <= 20000; i++) {
                ps.setObject(1, "name_" + i);
                ps.execute();
            }
            long end = System.currentTimeMillis();//20000:13076
            System.out.println(end - start);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(coon, ps);
        }

    }

    /*

   批量操作的方式三:
   一:
   1. addBatch():
   2. executeBatch():
   3. clearBatch():
   二: mysql服务器是关闭批处理的,我们需要通过一个参数,让mysql开启批处理的支持;
          ?rewriteBatchedStatements=true 写在配置文件的url后面
   三: 使用更新的mysql 驱动: mysql-connector-java-5.1.37-bin.jar
     */

    @Test
    public void testInsert2() {
        Connection coon = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            coon = JDBCUtils.getConnection();

            String sql = "insert into goods(name)values(?)";//预编译sql语句,只运行一次
            ps = coon.prepareStatement(sql);

            for (int i = 1; i <= 20000; i++) {
                ps.setObject(1, "name_" + i);

                // 1. '攒' sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //2. 执行batch
                    ps.executeBatch();

                    //3. 清空batch
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println(end - start);//20000: 376
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(coon, ps);
        }

    }

    //批量插入的方式四: 设置连接不允许自动提交数据
    @Test
    public void testInsert3() {
        Connection coon = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();

            coon = JDBCUtils.getConnection();

            //设置不允许自动提交数据
            coon.setAutoCommit(false);

            String sql = "insert into goods(name)values(?)";//预编译sql语句,只运行一次
            ps = coon.prepareStatement(sql);

            for (int i = 1; i <= 200000; i++) {
                ps.setObject(1, "name_" + i);

                // 1. '攒' sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //2. 执行batch
                    ps.executeBatch();

                    //3. 清空batch
                    ps.clearBatch();
                }
            }

            //统一提交数据
            coon.commit();

            long end = System.currentTimeMillis();
            System.out.println(end - start);//200000: 1307
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(coon, ps);
        }

    }
}
