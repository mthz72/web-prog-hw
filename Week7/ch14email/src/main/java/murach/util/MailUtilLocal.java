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
 * Slide 40-43: lop tien ich gui mail qua SMTP server chay ngay tren may minh
 * (localhost:25), loai khong doi dang nhap.
 *
 * Khac slide: import jakarta.mail.* thay vi javax.mail.* - tu Jakarta EE 9
 * toan bo package javax.* da doi ten thanh jakarta.* (xem ghi chu o pom.xml).
 *
 * Bon buoc gui mail (slide 17) luc nao cung la:
 *   1. lay mail session      2. tao message
 *   3. dat dia chi gui/nhan  4. gui di
 */
public class MailUtilLocal {

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML)
            throws MessagingException {

        // 1 - get a mail session
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", "25");

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
        // Slide 37: SMTP server khong doi xac thuc thi dung ham tinh send().
        Transport.send(message);
    }
}
