package si.um.feri.jsf;

import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.mail.MessagingException;
import lombok.Data;
import si.um.feri.dao.DoctorDao;
import si.um.feri.dao.DoctorDaoBean;
import si.um.feri.dao.PatientDao;
import si.um.feri.dao.PatientDaoBean;
import si.um.feri.vao.Patient;

import javax.naming.NamingException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named("patients")
@ViewScoped
@Data
public class PatientJSFBean implements Serializable {
    private final Date todayDate = new Date();
    Logger log = Logger.getLogger(PatientDaoBean.class.toString());

    @EJB
    private PatientDao patientDao;
    @EJB
    private DoctorDao doctorDao;

    private Patient patient = new Patient();
    private String selectedEmail;
    private String doctorEmail;

    public List<Patient> getAllPatients() {
        return patientDao.getAll();
    }

    public String savePatient() throws MessagingException, NamingException {
        patientDao.save(patient, doctorEmail);
        return "all";
    }

    public void setSelectedEmail(String email) {
        selectedEmail = email;
        patient = patientDao.find(email);
        if (patient == null) {
            patient = new Patient();
            return;
        }
        if(patient.getPersonalDoctor() != null) {
            doctorEmail = patient.getPersonalDoctor().getEmail();
        }
    }

    public void delete(Patient patient) {
        patientDao.delete(patient);
    }

    public List<Patient> getPatientsWithNoDoctor() {
        List<Patient> allPatients = getAllPatients();
        return allPatients.stream().filter(p -> p.getPersonalDoctor() == null).collect(Collectors.toList());
    }
}
