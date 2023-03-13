package si.um.feri;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.mail.MessagingException;
import si.um.feri.dao.DoctorDao;
import si.um.feri.dao.PatientDao;
import si.um.feri.dao.PatientDaoBean;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import javax.naming.NamingException;
import java.io.Serializable;
import java.util.logging.Logger;

@Stateless
public class SelectDoctorBean implements SelectDoctor, Serializable {

    @EJB
    DoctorDao doctorDao;

    @EJB
    PatientDao patientDao;
    Logger log = Logger.getLogger(PatientDaoBean.class.toString());
    @Override
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

    @Override
    public void selectDoctorString(String patientEmail, String doctorEmail) throws MessagingException, NamingException {
        log.info(patientEmail);
        log.info(doctorEmail);
        Patient patient = patientDao.find(patientEmail);
        Doctor doctor = doctorDao.find(doctorEmail);
        boolean free = doctor.getPatientQuota() > doctor.getPatientList().size();
        selectDoctor(patient, doctor, free);
    }

    @Override
    public void test(String test){
        log.info(test);
        log.info("EVO ME");
    }
}
