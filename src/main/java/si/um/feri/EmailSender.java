package si.um.feri;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import si.um.feri.dao.PatientDaoBean;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;
import java.util.logging.Logger;

public class EmailSender implements Serializable {
    static Logger log = Logger.getLogger(PatientDaoBean.class.toString());
    public static void send(String to, String subject, String body) throws NamingException, MessagingException {
        try {
            log.info("Sending E-mail");
            final String MAIL_REPLY_TO = "info@ehealth.com";
            Session session = (Session) new InitialContext().lookup("java:jboss/mail/ehealth");

            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setReplyTo(InternetAddress.parse(MAIL_REPLY_TO));
            message.setSubject(subject);
            message.setContent(body, "text/plain");
            Transport.send(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
