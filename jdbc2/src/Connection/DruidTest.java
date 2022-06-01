package Connection;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author shkstart
 * @create 2022-05-31-0:31
 */
public class DruidTest {

    @Test
    public void getConnection() throws Exception {

        Properties pros = new Properties();

        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");

        pros.load(is);

        DataSource source = DruidDataSourceFactory.createDataSource(pros);

        Connection conn = source.getConnection();

        System.out.println(conn);
    }
}
