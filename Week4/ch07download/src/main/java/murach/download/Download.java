package murach.download;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import jakarta.servlet.http.HttpSession;
import murach.business.User;
import murach.data.UserID;
import murach.util.CookieUtil;

public class Download extends HttpServlet{
    @Override 
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            // get current location
            String action = request.getParameter("action");
            if (action == null) {
                action = "viewAlbums";  // default action
            }
            //perform action and set URL to appropriate page
            String url = "/index.jsp";
            if (action.equals("viewAlbums")){
                url="/index.jsp";
            } else if  (action.equals("checkUser")){
                url = checkUser(request, response);
            }
            //forward to the view
            getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
        }
        @Override 
        public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            String action = request.getParameter("action");
            String url = "/index.jsp";
            if (action.equals("registerUser")){
                url = registerUser(request, response);
            }
            getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
        }
        private String checkUser(HttpServletRequest request, HttpServletResponse response){
            String productCode = request.getParameter("productCode");
            HttpSession session = request.getSession();
            session.setAttribute("productCode", productCode);
            User user = (User) session.getAttribute("user");

            String url;

            if (user == null){
                Cookie[] cookies = request.getCookies();
                String emailAddress = CookieUtil.getCookieValue(cookies, "emailCookie");
                if (emailAddress == null || emailAddress.equals("")){
                    url = "/register.jsp";
                } else {
                    ServletContext sc = getServletContext();
                    String path = sc.getRealPath("/WEB-INF/EmailList.txt");
                    user  = UserID.getUser(emailAddress, path);
                    session.setAttribute(user, user);
                    url = "/" + productCode + "_download.jsp";
                    return "/register.jsp";
                    }
                }
            else {
                url = "/" + productCode + "_download.jsp";  
            }
            return url;
        }
        private String registerUser(HttpServletRequest request, HttpServletResponse response){
            String emailAddress = request.getParameter("email");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");


            User user = new User(emailAddress, firstName, lastName);

            ServeletContext sc = getServletContext();
            String path = sc.getRealPath("/WEB-INF/EmailList.txt");
            UserID.add(user, path);
            Httpsession session = request.getSession();
            session.setAttribute("user", user);

            Cookie c = new Cookie("emailCookie", email);
            c.setMaxAge(60*60*24*365*2); // 2 years
            c.setPath("/");
            response.addCookie(c);

            String productCode = (String) session.getAttribute("productCode");
            String url = "/" + productCode + "_download.jsp";
            return url;
        }
}