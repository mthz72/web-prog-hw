package murach.data;

import java.sql.Connection;
import java.sql.SQLException;

// Hai import nay GIU NGUYEN javax: chung thuoc Java SE, khong phai Jakarta EE
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Slide 35-36: lop duy nhat duoc phep lay Connection.
 * Servlet va DAO khong bao gio goi DriverManager.getConnection().
 *
 * Pool that su do container quan ly:
 *   - Tomcat: META-INF/context.xml  (slide 34)
 *   - Jetty : WEB-INF/jetty-env.xml (dung khi chay mvn jetty:run)
 * Lop nay chi tra cuu no qua JNDI theo ten "jdbc/murach".
 *
 * Khac slide:
 *   - ten JNDI viet "java:comp/env/..." (chuan), slide viet "java:/comp/env/..."
 *   - getConnection() nem SQLException thay vi tra ve null, de noi goi
 *     khong bi NullPointerException khi DB chet
 *   - freeConnection(null) khong lam gi
 */
public class ConnectionPool {

    private static ConnectionPool pool = null;
    private static DataSource dataSource = null;

    private ConnectionPool() {
        try {
            InitialContext ic = new InitialContext();
            dataSource = (DataSource) ic.lookup("java:comp/env/jdbc/murach");
        } catch (NamingException e) {
            System.out.println(e);
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Khong tim thay DataSource 'jdbc/murach'. "
                    + "Kiem tra META-INF/context.xml (Tomcat) hoac WEB-INF/jetty-env.xml (Jetty).");
        }
        return dataSource.getConnection();
    }

    public void freeConnection(Connection c) {
        if (c == null) {
            return;
        }
        try {
            c.close();   // voi pool, close() la tra connection ve pool chu khong dong that
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
