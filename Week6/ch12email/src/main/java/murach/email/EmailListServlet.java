package murach.email;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import murach.business.User;
import murach.data.UserDB;

/**
 * Slide 42-44: Controller. Khong import java.sql, khong tao connection.
 * Moi thao tac CSDL deu goi qua murach.data.UserDB.
 */
public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = "/index.jsp";

        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "join";  // default action
        }

        // perform action and set URL to appropriate page
        if (action.equals("join")) {
            url = "/index.jsp";    // the "join" page
        } else if (action.equals("add")) {
            // get parameters from the request
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            // store data in User object
            User user = new User(firstName, lastName, email);

            // validate the parameters
            String message;   // slide 44 dung bien nay ma quen khai bao
            if (UserDB.emailExists(user.getEmail())) {
                message = "This email address already exists.<br>"
                        + "Please enter another email address.";
                url = "/index.jsp";
            } else if (UserDB.insert(user) == 0) {
                // slide khong kiem tra: DB loi thi insert tra 0, nguoi dung
                // van thay trang cam on du khong co gi duoc luu
                message = "Sorry, we could not save your information. "
                        + "Please try again later.";
                url = "/index.jsp";
            } else {
                message = "";
                url = "/thanks.jsp";
            }
            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
