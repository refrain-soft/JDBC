package DAO2;

import util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: data(base) access object
 *
 * @author shkstart
 * @create 2022-05-29-19:02
 * 封装了针对于数据表的通用的操作
 */
public abstract class BaseDAO<T> {

    private Class<T> clazz = null;

    {
        //获取当前对象所在类带泛型的父类
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;

        Type[] typeArguments = parameterizedType.getActualTypeArguments();//获取了父类的泛型的参数
        clazz = (Class<T>) typeArguments[0];//获取泛型的第一个参数
    }

    //通用的增删改操作 --- version 2.0(考虑上事务)
    public int update(Connection conn, String sql, Object... args) {

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


    //通用的查询操作, 用于返回数据表的一条记录; (version 2.0 考虑上事务)
    public T getInstance(Connection conn, String sql, Object... args) {
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

    //通用的查询操作, 用于返回数据表的多条记录构成的集合; (version 2.0 考虑上事务)
    public List<T> getForList(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            //创建集合对象
            ArrayList<T> list = new ArrayList<>();

            while (rs.next()) {
                T t = clazz.getDeclaredConstructor().newInstance();
                //处理一行结果集一行数据中的每一个列: 给t对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个类的类名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给t这个对象指定的columnName属性,赋值为columnValue: 通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);

                }
                list.add(t);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }


    //用于查询特殊值的通用方法
    public <T> T getValue(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);

            }
            rs = ps.executeQuery();

            if (rs.next()) {
                return (T) rs.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;

    }
}

