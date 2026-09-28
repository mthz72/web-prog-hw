package murach.sql;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import murach.business.SQLResult;
import murach.data.SQLGatewayDB;

/**
 * Controller cua SQL Gateway, sau khi refactor.
 *
 * Truoc: servlet tu nap driver, tu DriverManager.getConnection(), tu tao Statement.
 * Sau  : servlet KHONG import java.sql. Moi viec voi CSDL giao cho murach.data.SQLGatewayDB,
 *        connection lay tu pool (murach.data.ConnectionPool) do container quan ly.
 */
public class SQLGatewayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sqlStatement = request.getParameter("sqlStatement");
        if (sqlStatement == null) {
            sqlStatement = "";
        }
        sqlStatement = sqlStatement.trim();

        String sqlResult = "";
        if (sqlStatement.length() >= 6) {
            SQLResult result = SQLGatewayDB.execute(sqlStatement);
            sqlResult = SQLUtil.toHtml(result);
        }

        HttpSession session = request.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        String url = "/index.jsp";
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    /** Go thang URL /sqlGateway tren trinh duyet thi quay ve form, khong bi 405. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        getServletContext()
                .getRequestDispatcher("/index.jsp")
                .forward(request, response);
    }
}
