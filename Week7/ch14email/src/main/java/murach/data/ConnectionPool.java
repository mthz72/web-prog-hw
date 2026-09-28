package murach.data;

import java.sql.Connection;
import java.sql.SQLException;

// Hai import nay GIU NGUYEN javax: chung thuoc Java SE, khong phai Jakarta EE
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Lop duy nhat duoc phep lay Connection / DataSource.
 * Servlet va DAO khong bao gio goi DriverManager.getConnection().
 *
 * Pool that su do container quan ly:
 *   - Tomcat: META-INF/context.xml
 *   - Jetty : WEB-INF/jetty-env.xml (dung khi chay mvn jetty:run)
 * Lop nay chi tra cuu no qua JNDI theo ten "jdbc/murach".
 *
 * Khac Week6: them getDataSource(). JPA khong xin tung Connection mot ma can
 * ca DataSource de tu quan ly vong doi connection, nen JPAUtil se lay
 * DataSource nay dua thang cho Hibernate. Nho vay JPA dung CHUNG connection
 * pool cua container chu khong tu mo pool rieng - day la diem chinh cua bai.
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

    /** Dung cho JPA (xem JPAUtil). */
    public DataSource getDataSource() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Khong tim thay DataSource 'jdbc/murach'. "
                    + "Kiem tra META-INF/context.xml (Tomcat) hoac WEB-INF/jetty-env.xml (Jetty).");
        }
        return dataSource;
    }

    /** Van giu lai cho code JDBC thuan (Week6) neu can so sanh. */
    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
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
