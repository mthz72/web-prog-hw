package murach.business;

import java.util.ArrayList;
import java.util.List;

/**
 * Ket qua cua mot cau SQL, doc xong tu ResultSet roi.
 * Tang tren (servlet, SQLUtil) chi lam viec voi lop nay, khong cham vao java.sql.
 */
public class SQLResult {

    private boolean query;                       // true neu la SELECT
    private List<String> columnNames = new ArrayList<>();
    private List<List<String>> rows = new ArrayList<>();
    private int rowsAffected;                    // cho INSERT/UPDATE/DELETE/DDL
    private String errorMessage;                 // != null neu SQL loi

    public boolean isQuery() {
        return query;
    }

    public void setQuery(boolean query) {
        this.query = query;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public int getRowsAffected() {
        return rowsAffected;
    }

    public void setRowsAffected(int rowsAffected) {
        this.rowsAffected = rowsAffected;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isError() {
        return errorMessage != null;
    }
}
