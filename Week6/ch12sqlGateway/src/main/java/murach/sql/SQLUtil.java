package murach.sql;

import java.util.List;

import murach.business.SQLResult;

/**
 * Slide 29-31, sau khi refactor: chi con viec bien SQLResult thanh HTML.
 * Phan doc ResultSet / ResultSetMetaData da chuyen sang murach.data.SQLGatewayDB,
 * nen lop nay khong con import java.sql.
 */
public class SQLUtil {

    public static String toHtml(SQLResult result) {
        if (result.isError()) {
            return "<p>Error executing the SQL statement: <br>"
                    + escape(result.getErrorMessage()) + "</p>";
        }
        if (result.isQuery()) {
            return getHtmlTable(result);
        }
        if (result.getRowsAffected() == 0) {   // cau lenh DDL
            return "<p>The statement executed successfully.</p>";
        }
        return "<p>The statement executed successfully.<br>"   // INSERT, UPDATE, DELETE
                + result.getRowsAffected() + " row(s) affected.</p>";
    }

    public static String getHtmlTable(SQLResult result) {
        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append("<table>");

        // dong tieu de
        htmlTable.append("<tr>");
        for (String columnName : result.getColumnNames()) {
            htmlTable.append("<th>");
            htmlTable.append(escape(columnName));
            htmlTable.append("</th>");
        }
        htmlTable.append("</tr>");

        // cac dong du lieu
        for (List<String> row : result.getRows()) {
            htmlTable.append("<tr>");
            for (String value : row) {
                htmlTable.append("<td>");
                htmlTable.append(escape(value));
                htmlTable.append("</td>");
            }
            htmlTable.append("</tr>");
        }

        htmlTable.append("</table>");
        return htmlTable.toString();
    }

    /** Slide khong co. De du lieu chua '<' hay '&' khong pha vo HTML. */
    private static String escape(String s) {
        if (s == null) {
            return "null";
        }
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
