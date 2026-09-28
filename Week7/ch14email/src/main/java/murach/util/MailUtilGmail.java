package murach.util;

import java.util.Properties;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Slide 52-55: gui mail qua SMTP server o xa (Gmail), loai BAT BUOC dang nhap.
 *
 * Khac slide o hai cho:
 *  1. import jakarta.mail.* thay vi javax.mail.*  (Jakarta EE 9).
 *  2. Slide viet thang transport.connect("johnsmith@gmail.com", "sesame") -
 *     tai khoan va mat khau nam cung trong file .java. Nhu vay la dua mat khau
 *     len git. O day username/password duoc truyen vao tu ngoai; servlet doc
 *     chung tu <context-param> trong web.xml.
 *
 * Luu y khi chay that: tu 2022 Gmail khong con cho dang nhap bang mat khau
 * thuong nua. Phai bat xac thuc 2 buoc cho tai khoan roi tao mot
 * "App password" (16 ky tu) va dung chuoi do lam password o day.
 */
public class MailUtilGmail {

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML,
            String username, String password)
            throws MessagingException {

        // 1 - get a mail session
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.port", "465");
        props.put("mail.smtps.auth", "true");
        // Slide 19: dat false de tranh SSLException khi ngat ket noi voi Gmail
        props.put("mail.smtps.quitwait", "false");

        // Khac slide: getInstance() chu KHONG phai getDefaultInstance().
        // getDefaultInstance() tra ve mot Session dung chung ca JVM, va
        // properties cua no bi dong bang tu LAN GOI DAU TIEN. Vi MailUtil co
        // the doi qua lai giua local va gmail luc dang chay, dung
        // getDefaultInstance() thi ai chay truoc se gianh luon cau hinh:
        // chay local roi moi chay gmail -> gmail van gui vao localhost:25 va
        // that bai. Chinh la cai bay ma slide 25 mo ta bang cau "phai restart
        // Tomcat thi doi properties moi co hieu luc". getInstance() tao
        // Session rieng moi lan nen khong con van de do.
        Session session = Session.getInstance(props);
        session.setDebug(true);

        // 2 - create a message
        Message message = new MimeMessage(session);
        message.setSubject(subject);
        if (bodyIsHTML) {
            message.setContent(body, "text/html");
        } else {
            message.setText(body);
        }

        // 3 - address the message
        Address fromAddress = new InternetAddress(from);
        Address toAddress = new InternetAddress(to);
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);

        // 4 - send the message
        // Slide 37: server doi xac thuc -> phai tu mo Transport, connect,
        // sendMessage roi close chu khong dung duoc Transport.send().
        Transport transport = session.getTransport();
        try {
            transport.connect(username, password);
            transport.sendMessage(message, message.getAllRecipients());
        } finally {
            // Slide khong co finally: neu sendMessage loi thi connection
            // toi Gmail bi bo lung lung.
            transport.close();
        }
    }
}
