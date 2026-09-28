package murach.util;

import jakarta.mail.MessagingException;

/**
 * Chon cach gui mail luc chay, dua vao cau hinh (bien moi truong hoac
 * <context-param> trong web.xml). Servlet nho vay khong phai sua code khi doi
 * moi truong.
 *
 * Lop nay KHONG co trong slide. Them vao vi ba ly do:
 *  - de tai khoan/mat khau ra khoi file .java (xem MailUtilGmail)
 *  - co che do "off" cho may khong cai duoc SMTP server
 *  - o may thi gui bang Gmail duoc, nhung tren Render (goi free) thi port
 *    465/587/25 bi chan het, phai chuyen sang port 2525 -> can doi cach gui
 *    ma khong doi code servlet
 */
public class MailUtil {

    public static final String MODE_LOCAL = "local";
    public static final String MODE_GMAIL = "gmail";
    public static final String MODE_SMTP = "smtp";
    public static final String MODE_OFF = "off";

    private final String mode;
    private final String username;
    private final String password;
    private final String host;
    private final int port;
    private final boolean startTls;

    /** Dung cho che do local / gmail / off (khong can host, port). */
    public MailUtil(String mode, String username, String password) {
        this(mode, username, password, null, 0, true);
    }

    public MailUtil(String mode, String username, String password,
            String host, int port, boolean startTls) {
        this.mode = (mode == null || mode.isBlank()) ? MODE_LOCAL : mode.trim().toLowerCase();
        this.username = (username == null) ? null : username.trim();
        // Google hien App password duoi dang 4 nhom "abcd efgh ijkl mnop" cho de
        // doc, nhung mat khau that la 16 ky tu lien nhau. Bo het khoang trang de
        // copy nguyen van tu trang Google van dang nhap duoc.
        this.password = (password == null) ? null : password.replaceAll("\s+", "");
        this.host = (host == null) ? null : host.trim();
        this.port = port;
        this.startTls = startTls;
    }

    public String getMode() {
        return mode;
    }

    public boolean isEnabled() {
        return !MODE_OFF.equals(mode);
    }

    public void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML)
            throws MessagingException {

        switch (mode) {
            case MODE_OFF:
                return;                       // khong gui gi ca
            case MODE_GMAIL:
                MailUtilGmail.sendMail(to, from, subject, body, bodyIsHTML,
                        username, password);
                return;
            case MODE_SMTP:
                // Che do dung khi deploy len Render (Brevo/SendGrid port 2525)
                MailUtilSmtp.sendMail(to, from, subject, body, bodyIsHTML,
                        host, port, startTls, username, password);
                return;
            default:
                MailUtilLocal.sendMail(to, from, subject, body, bodyIsHTML);
        }
    }
}
