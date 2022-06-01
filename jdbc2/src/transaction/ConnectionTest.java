package transaction;

import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.sql.Connection;

/**
 * @author shkstart
 * @create 2022-05-28-18:45
 */
public class ConnectionTest {
    @Test
    public void testGetConnection() throws Exception {
        Connection conn = JDBCUtils.getConnection();

        System.out.println(conn);
    }

}
