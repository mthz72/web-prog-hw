package murach.email;

import java.io.IOException;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import murach.business.User;
import murach.data.UserDB;
import murach.util.MailUtil;

/**
 * Slide 45-51: Controller.
 *
 * Servlet khong biet gi ve CSDL (khong import java.sql, khong mo connection,
 * khong biet co JPA hay khong) va cung khong biet gi ve SMTP. No chi goi
 * UserDB va MailUtil. Doi tang duoi tu JDBC sang JPA o Week7 nay khong lam
 * thay doi mot dong nao trong luong xu ly ben duoi.
 */
public class EmailListServlet extends HttpServlet {

    // Doc mot lan luc servlet khoi tao, khong doc lai moi request
    private MailUtil mailUtil;
    private String fromAddress;

    @Override
    public void init() throws ServletException {
        var context = getServletContext();

        // Uu tien bien moi truong, sau do moi den <context-param> trong web.xml.
        // Ly do: mat khau App password cua Gmail KHONG duoc nam trong web.xml,
        // vi file do bi commit len git. De trong web.xml va dat bien moi truong
        // MAIL_PASSWORD luc chay:
        //   Windows (PowerShell):  $env:MAIL_PASSWORD = "..."; mvn jetty:run
        //   Linux / macOS:         MAIL_PASSWORD="..." mvn jetty:run
        // port 465 dung SSL ngay tu dau; 587 / 2525 thi ket noi thuong roi
        // nang cap len TLS bang STARTTLS
        int mailPort = intConfig(context, "MAIL_PORT", "mail.port", 2525);
        mailUtil = new MailUtil(
                config(context, "MAIL_MODE", "mail.mode"),
                config(context, "MAIL_USERNAME", "mail.username"),
                config(context, "MAIL_PASSWORD", "mail.password"),
                config(context, "MAIL_HOST", "mail.host"),
                mailPort,
                mailPort != 465);

        fromAddress = config(context, "MAIL_FROM", "mail.from");
        if (fromAddress == null || fromAddress.isBlank()) {
            fromAddress = "email_list@murach.com";   // slide 49
        }
    }

    /** Doc bien moi truong envName truoc, khong co thi lay <context-param> paramName. */
    private static String config(jakarta.servlet.ServletContext context,
            String envName, String paramName) {
        String value = System.getenv(envName);
        if (value == null || value.isBlank()) {
            value = context.getInitParameter(paramName);
        }
        return value;
    }

    /** Nhu config() nhung tra ve so; cau hinh sai dinh dang thi dung defaultValue. */
    private static int intConfig(jakarta.servlet.ServletContext context,
            String envName, String paramName, int defaultValue) {
        String value = config(context, envName, paramName);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

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
            String message;
            if (UserDB.emailExists(user.getEmail())) {
                message = "This email address already exists.<br>"
                        + "Please enter another email address.";
                url = "/index.jsp";
            } else if (UserDB.insert(user) == 0) {
                // Slide khong kiem tra: DB loi thi insert that bai, nguoi dung
                // van thay trang cam on du khong co gi duoc luu.
                message = "Sorry, we could not save your information. "
                        + "Please try again later.";
                url = "/index.jsp";
            } else {
                message = "";
                url = "/thanks.jsp";
                sendWelcomeEmail(request, user);
            }
            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    /**
     * Slide 49-51. Luu y: loi gui mail KHONG lam hong request. Nguoi dung da
     * ghi danh thanh cong roi, nen van cho ho sang thanks.jsp va chi bao them
     * mot dong loi - chu khong bat ho nhap lai tu dau.
     */
    private void sendWelcomeEmail(HttpServletRequest request, User user) {
        String to = user.getEmail();
        String from = fromAddress;
        String subject = "Welcome to our email list";
        String body = "Dear " + user.getFirstName() + ",\n\n"
                + "Thanks for joining our email list. "
                + "We'll make sure to send you announcements "
                + "about new products and promotions.\n\n"
                + "Have a great day and thanks again!\n\n"
                + "Kelly Slivkoff\n"
                + "Mike Murach & Associates";
        boolean isBodyHTML = false;

        if (!mailUtil.isEnabled()) {
            request.setAttribute("mailNotice",
                    "NOTE: Email sending is turned off "
                    + "(mail.mode = off in web.xml). Nothing was sent.");
            return;
        }

        try {
            mailUtil.sendMail(to, from, subject, body, isBodyHTML);
        } catch (MessagingException e) {
            String errorMessage
                    = "ERROR: Unable to send email. "
                    + "Check the server logs for details.<br>"
                    + "NOTE: You may need to configure your system "
                    + "as described in chapter 14.<br>"
                    + "ERROR MESSAGE: " + e.getMessage();
            request.setAttribute("errorMessage", errorMessage);

            // Slide 51: ghi lai noi dung mail vao log de khong mat du lieu
            this.log("Unable to send email.\n"
                    + "Here is the email you tried to send:\n"
                    + "=====================================\n"
                    + "TO: " + to + "\n"
                    + "FROM: " + from + "\n"
                    + "SUBJECT: " + subject + "\n"
                    + body + "\n", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
