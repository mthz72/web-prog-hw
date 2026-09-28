package murach.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import murach.business.SQLResult;

/**
 * Lop truy cap du lieu (DAO) cua SQL Gateway.
 * Toan bo JDBC truoc day nam trong SQLGatewayServlet (slide 25-27) duoc chuyen vao day.
 *
 * Khac UserDB: SQL Gateway chay cau lenh tuy y nguoi dung go, nen khong the
 * dung PreparedStatement voi tham so ? — phai dung Statement.
 */
public class SQLGatewayDB {

    public static SQLResult execute(String sqlStatement) {
        SQLResult result = new SQLResult();

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = pool.getConnection();
            statement = connection.createStatement();

            if (sqlStatement.regionMatches(true, 0, "select", 0, 6)) {
                resultSet = statement.executeQuery(sqlStatement);
                result.setQuery(true);

                // slide 31: dung ResultSetMetaData de biet so cot va ten cot
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    result.getColumnNames().add(metaData.getColumnName(i));
                }
                while (resultSet.next()) {
                    List<String> row = new ArrayList<>(columnCount);
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(resultSet.getString(i));
                    }
                    result.getRows().add(row);
                }
            } else {
                result.setRowsAffected(statement.executeUpdate(sqlStatement));
            }
        } catch (SQLException e) {
            result.setErrorMessage(e.getMessage());
        } finally {
            DBUtil.closeResultSet(resultSet);
            DBUtil.closeStatement(statement);
            pool.freeConnection(connection);
        }
        return result;
    }
}
