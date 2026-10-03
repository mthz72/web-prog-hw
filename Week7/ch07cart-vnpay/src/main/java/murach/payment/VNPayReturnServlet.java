package murach.payment;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * VNPay chuyen khach ve day sau khi thanh toan.
 * Phai kiem tra chu ky truoc, vi ai cung co the tu sua cac tham so tren URL.
 */
public class VNPayReturnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> fields = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII);
            String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        String signValue = VNPayConfig.hashAllFields(fields);

        boolean validSignature = signValue.equalsIgnoreCase(vnp_SecureHash);
        boolean success = validSignature
                && "00".equals(request.getParameter("vnp_ResponseCode"))
                && "00".equals(request.getParameter("vnp_TransactionStatus"));

        if (success) {
            request.getSession().removeAttribute("cart");
        }

        long amount = 0;
        try {
            amount = Long.parseLong(request.getParameter("vnp_Amount")) / 100;
        } catch (NumberFormatException e) {
            // de 0
        }

        request.setAttribute("validSignature", validSignature);
        request.setAttribute("success", success);
        request.setAttribute("amount", amount);
        getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
    }
}
