package transaction;

import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author shkstart
 * @create 2022-05-28-18:51
 */

/*
 1. 什么叫数据库事务?
  事务: 一组逻辑操作单元,是数据从一种状态变换到另一种状态
          一组逻辑操作单元,一个或多个DML操作

 2. 事务处理的原则:保证所有事务都作为一个工作单元来执行,即便出现了故障,都不能改变这种执行方式
    当在一个事务中执行多个操作时:
      要么所有的事务都被提交(commit),那么这些被修改就永久的保存下来;
      要么数据库管理系统将放弃所作的所有修改,整个事务回滚(rollback)到最初的状态

 3.数据一旦提交,就不可回滚

 4.哪些操作会导致数据的自动提交?
      DDL操作一旦执行,都会自动提交
         set autocommit = false 对DDL操作失败

      DML操作默认情况下,一旦执行,就会自动提交
         可以通过 set autocommit = false的方式,取消DML操作的自动提交

      默认在关闭连接时,会自动的提交数据

 5. 事务的ACID属性
   ①: 原子性(Atomicity): 事务时不可分割的工作单位
   ②: 一致性(Consistency): 从一个一致性状态变换到另外一个一致性状态
   ③: 隔离性(Isolation): 各个事务之间不互相干扰
   ④: 持久性(Durability): 事务一旦提交,对数据库中数据的改变时永久性的;后续的操作和数据库故障不应该对其造成影响

    数据库的并发问题
      脏读: 对于两个事务T1,T2, T1读取了已经被T2更新但还没有被提交的字段; 之后,若T2回滚,T1读取的内容就是临时且无效的

      不可重复读: 对于两个事务T1,T2, T1读取了一个字段,然后T2更新了该字段; 之后,T1再次读取同一个字段,值就不同了

      幻读: 对于两个事务T1,T2, T1从表中读取了一个字段,然后T2在该表中插入了一些新的行; 之后,如果T1再次读取同一个表,就会多出几行


 */
public class TransactionTest {


    //*****************未考虑数据库事务情况下的转账操作*******************
    /*
    针对于数据表user_table表实现: 代广向邓林转账100
    
    update user_table set balance = balance - 100 where user = '邓林';
    
    update user_table set balance = balance + 100 where user = '代广';
     */
    @Test
    public void testUpdate() {
        String sql1 = "update user_table set balance = balance - 100 where user = ?";
        update(sql1, "邓林");


        //模拟网络异常
        System.out.println(10 / 0);

        String sql2 = "update user_table set balance = balance + 100 where user = ?";
        update(sql2, "代广");

        System.out.println("转账成功");
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
            JDBCUtils.closeResource(connection, ps);
        }
        return 0;
    }

    //*****************考虑数据库事务情况下的转账操作*******************


    @Test
    public void testUpdateWithTx() {
        Connection coon = null;
        try {
            coon = JDBCUtils.getConnection();
            System.out.println(coon.getAutoCommit());//true

            //设置取消数据自动提交功能
            coon.setAutoCommit(false);

            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            update(coon, sql1, "邓林");


            //模拟网络异常
            System.out.println(10 / 0);

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(coon, sql2, "代广");

            System.out.println("转账成功");

            //上面操作成功了,再提交数据
            coon.commit();

        } catch (Exception e) {
            e.printStackTrace();
            //回滚数据
            try {
                coon.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } finally {
            //修改其为自动提交数据
            // 主要针对域使用数据库连接池的使用
            try {
                coon.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JDBCUtils.closeResource(coon, null);
        }

    }


    //通用的增删改操作 --- version 2.0(考虑上事务)
    public static int update(Connection conn, String sql, Object... args) {

        //通用的增删改操作
        PreparedStatement ps = null;
        try {
            //1. 预编译sql语句.返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);

            //2. 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //3. 执行
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
            //4. 资源的关闭
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }


    //**********************************************

    @Test
    public void testTransactionSelect() {

        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            //获取当前连接的隔离级别;
            System.out.println(conn.getTransactionIsolation());// 4

            //设置数据库的隔离级别;避免脏读问题
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);// 2

            //取消自动提交数据
            conn.setAutoCommit(false);

            String sql = "select user,password,balance,birth from user_table where user =?";

            User dg = getInstance(conn, User.class, sql, "代广");
            System.out.println(dg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    @Test
    public void testTransactionUpdate() {

        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            String sql = "update user_table set balance = ? where user = ?";
            update(conn, sql, 2000, "代广");

            Thread.sleep(10000);
            System.out.println("修改结束");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    //通用的查询操作, 用于返回数据表的一条记录; (version 2.0 考虑上事务)
    public <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            ps = conn.prepareStatement(sql);

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
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
}
