package murach.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Slide 52-53: dong Statement / ResultSet an toan trong khoi finally.
 * Nhan null duoc, va nuot SQLException de khong che mat loi goc.
 */
public class DBUtil {

    public static void closeStatement(Statement s) {
        try {
            if (s != null) {
                s.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void closePreparedStatement(Statement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
