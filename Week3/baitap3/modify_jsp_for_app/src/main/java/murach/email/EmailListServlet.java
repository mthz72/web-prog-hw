package murach.email;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import murach.business.User;

public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String dob = request.getParameter("dob");
        String source = request.getParameter("source");
        String contact = request.getParameter("contact");

        // Buoc 6: lay nam hien tai duoi dang int va dat vao request
        // (thay cho scriptlet truoc day nam trong footer.jsp)
        int currentYear = LocalDate.now().getYear();
        request.setAttribute("currentYear", currentYear);

        String url;
        if (email == null || email.isEmpty()
                || firstName == null || firstName.isEmpty()
                || lastName == null || lastName.isEmpty()) {

            log("Validation failed: Missing fields.");
            request.setAttribute("errorMessage",
                    "Error: Please enter your email, First Name and Last Name!");
            request.setAttribute("email", email);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("dob", dob);
            url = "/index.jsp";
        } else {
            User user = new User(email, firstName, lastName);
            request.setAttribute("user", user);
            request.setAttribute("dob", dob);
            request.setAttribute("source", source);
            request.setAttribute("contact", contact);
            request.setAttribute("action", action);
            url = "/thanks.jsp";
        }

        getServletContext().getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
