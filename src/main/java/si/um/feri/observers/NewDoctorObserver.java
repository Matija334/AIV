package si.um.feri.observers;

import jakarta.mail.MessagingException;
import lombok.Data;
import si.um.feri.EmailSender;
import si.um.feri.dao.PatientDaoBean;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import javax.naming.NamingException;
import java.util.logging.Logger;

@Data
public class NewDoctorObserver implements Observer{

    private Doctor doctor;
    private Patient patient;

    public NewDoctorObserver(Doctor doctor, Patient patient) {
        this.doctor = doctor;
        this.patient = patient;
    }

    Logger log = Logger.getLogger(PatientDaoBean.class.toString());
    @Override
    public void action() throws MessagingException, NamingException {
        log.info("New doctor assigned: " + doctor.getName() + " " + doctor.getLastName());
        log.info("Posiljam email observer hello");
        EmailSender.send(patient.getEmail(), "USPESNA DODELITEV", "Dodeljen vam je bil doktor: " + doctor.getName() + " " + doctor.getLastName());
    }
}
