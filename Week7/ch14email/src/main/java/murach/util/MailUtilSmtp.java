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
 * Ban TONG QUAT cua MailUtilGmail: host / port / xac thuc deu truyen vao,
 * khong viet cung smtp.gmail.com:465.
 *
 * Van la dung 4 buoc cua slide 17, khong them buoc nao. Chi khac o buoc 1:
 * Properties duoc dung tu tham so thay vi ghi san.
 *
 * VI SAO CAN LOP NAY khi deploy len Render:
 * Render o goi free CHAN toan bo outbound tra port 25, 465 va 587 - dung ba
 * port SMTP chuan. Nen MailUtilLocal (25) va MailUtilGmail (465) deu khong
 * gui duoc sau khi deploy.
 * Cach di duong khac: cac nha cung cap nhu Brevo / SendGrid con mo SMTP o
 * port 2525, va Render khong chan port do. Nho vay giu nguyen 100% JavaMail
 * cua chuong 14, chi doi host + port.
 *
 * Hai kieu bao mat, chon theo port:
 *   - port 465        -> SSL ngay tu dau  (protocol "smtps")
 *   - 587 / 2525 / 25 -> ket noi thuong roi NANG CAP len TLS bang STARTTLS
 *                        (protocol "smtp" + mail.smtp.starttls.enable)
 * Dat starttls.required = true de neu server khong ho tro TLS thi bao loi,
 * chu khong am tham gui mat khau di duoi dang chua ma hoa.
 */
public class MailUtilSmtp {

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML,
            String host, int port, boolean useStartTls,
            String username, String password)
            throws MessagingException {

        // 1 - get a mail session
        Properties props = new Properties();
        if (useStartTls) {
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", String.valueOf(port));
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
        } else {
            props.put("mail.transport.protocol", "smtps");
            props.put("mail.smtps.host", host);
            props.put("mail.smtps.port", String.valueOf(port));
            props.put("mail.smtps.auth", "true");
            props.put("mail.smtps.quitwait", "false");
        }

        // getInstance() chu khong phai getDefaultInstance() - xem ghi chu
        // trong MailUtilLocal ve cai bay Session dung chung ca JVM.
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

        // 4 - send the message (slide 37: server doi xac thuc)
        Transport transport = session.getTransport();
        try {
            transport.connect(username, password);
            transport.sendMessage(message, message.getAllRecipients());
        } finally {
            transport.close();
        }
    }
}
