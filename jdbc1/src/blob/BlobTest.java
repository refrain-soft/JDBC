package blob;

import Bean.Student;
import Util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author shkstart
 * @create 2022-05-26-22:11
 * 测试使用PreparedStatement操作Blob类型的数据
 */
public class BlobTest {

    //向数据表examstudent表中插入Blob类型的字段
    @Test
    public void testInsert() {
        Connection coon = null;
        PreparedStatement ps = null;
        FileInputStream fis = null;
        try {
            coon = JDBCUtils.getConnection();

            String sql = "insert into student(name,grade,img) values(?,?,?)";
            ps = coon.prepareStatement(sql);

            /*
            //默认图片大小为4M ;图片超过4M情况: -> C:\ProgramData\MySQL\MySQL Server 5.7 -> 修改my.ini文件274行为16M
            ps.setObject(1, "代广");
            ps.setObject(2, 80);

            fis = new FileInputStream("img.png");
            ps.setBlob(3, fis);
             */
            ps.setObject(1, "邓林");
            ps.setObject(2, 90);

            fis = new FileInputStream("英雄.png");
            ps.setBlob(3, fis);

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(coon, ps);
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //查询数据student表中的Blob类型的字段
    @Test
    public void testQuery() {
        Connection coon = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            coon = JDBCUtils.getConnection();

            String sql = "select id,name,grade,img from student where id=?";
            ps = coon.prepareStatement(sql);

            ps.setObject(1, 7);

            rs = ps.executeQuery();

            if (rs.next()) {
                //方式一:
                //            int id = rs.getInt(1);
                //            String name = rs.getString(2);
                //            int grade = rs.getInt(3);

                //方式二:
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int grade = rs.getInt("grade");

                Student stu = new Student(id, name, grade);
                System.out.println(stu);

                //将Blob类型的字段下载下来,以文件的形式存储到本地
                Blob img = rs.getBlob("img");

                is = img.getBinaryStream();
                fos = new FileOutputStream("img1.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(coon, ps, rs);
        }
    }
}
