package murach.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import murach.business.User;

public class SurveyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Buoc 4: lay sau tham so tu request, xu ly, dua vao doi tuong User
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String heardFrom = request.getParameter("heardFrom");

        // Checkbox khong duoc tick thi khong co trong request -> null
        String updates = request.getParameter("updates") == null ? "No" : "Yes";

        String contactVia = request.getParameter("contactVia");

        User user = new User(email, firstName, lastName, heardFrom, updates, contactVia);

        // ... roi forward sang survey.jsp
        request.setAttribute("user", user);
        getServletContext().getRequestDispatcher("/survey.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
