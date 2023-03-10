package si.um.feri;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import si.um.feri.dao.PatientMemoryDao;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.logging.Logger;

public class EmailSender {
    Logger log = Logger.getLogger(PatientMemoryDao.class.toString());
    public void send(Patient patient) throws NamingException, MessagingException {
        log.info("Posiljam email");
        final String MAIL_REPLY_TO="info@ehealth.com";
        Session session = (Session)new InitialContext().lookup("java:jboss/mail/ehealth");

        Message message = new MimeMessage(session);
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(patient.getEmail()));
        message.setReplyTo(InternetAddress.parse(MAIL_REPLY_TO));
        message.setSubject("ZAVRNITEV");
        message.setContent("Izbran zdravnik ima zapolnjeno kapaciteto. Prosim izberite drugega osebnega zdravnika!", "text/plain");
        Transport.send(message);

    }
    public void send(Patient patient, Doctor doctor) throws NamingException, MessagingException {
        log.info("Posiljam email");
        final String MAIL_REPLY_TO="info@ehealth.com";
        Session session = (Session)new InitialContext().lookup("java:jboss/mail/ehealth");

        Message message_patient = new MimeMessage(session);
        message_patient.setRecipients(Message.RecipientType.TO, InternetAddress.parse(patient.getEmail()));
        message_patient.setReplyTo(InternetAddress.parse(MAIL_REPLY_TO));
        message_patient.setSubject("USPESNA IZBIRA");
        message_patient.setContent("Uspesno ste si izbrali osebnega zdravnika!", "text/plain");
        Transport.send(message_patient);

        Message message_doctor = new MimeMessage(session);
        message_doctor.setRecipients(Message.RecipientType.TO, InternetAddress.parse(doctor.getEmail()));
        message_doctor.setReplyTo(InternetAddress.parse(MAIL_REPLY_TO));
        message_doctor.setSubject("NOVI PACIENT");
        message_doctor.setContent(patient.getName() + " " + patient.getLastName() + " vas je izbral kot osebnega zdravnika!", "text/plain");
        Transport.send(message_doctor);

    }
}
