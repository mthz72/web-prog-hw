package murach.email;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Thank you for your survey</title></head><body>");
        out.println("<p>Hello: " + firstName + " " + lastName + "</p>");
        out.println("<p>Your email: " + email + "</p>");
        out.println("<p>Date of birth: " + dob + "</p>");
        out.println("<p>You know us from: " + source + "</p>");
        out.println("<p>Contact: " + contact + "</p>");
        out.println("</body></html>");
        out.println("<p><a href='/'>Back to Survey</a></p>");
    }
}