package si.um.feri;

import jakarta.mail.MessagingException;
import si.um.feri.dao.PatientMemoryDao;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import javax.naming.NamingException;
import java.util.logging.Logger;

public class Vmesni {
    Logger log = Logger.getLogger(PatientMemoryDao.class.toString());
    public Patient selectDoctor(Patient patient, Doctor doctor, boolean free) throws MessagingException, NamingException {
        if(free){
            patient.setPersonalDoctor(doctor);
            log.info("Uspesno dodan");
            new EmailSender().send(patient, doctor);
        } else{
            log.info("NAPAKAA");
            new EmailSender().send(patient);
        }
        return patient;
    }
}
