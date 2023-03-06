package si.um.feri.jsf;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import si.um.feri.dao.DoctorMemoryDao;
import si.um.feri.dao.PatientMemoryDao;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named("patients")
@ViewScoped
@Data
public class PatientJSFBean implements Serializable {
    private static final long serialVersionUID = -8979220536758073133L;
    private final Date todayDate = new Date();
    Logger log = Logger.getLogger(PatientMemoryDao.class.toString());

    private PatientMemoryDao patientDao = PatientMemoryDao.getInstance();
    private DoctorMemoryDao doctorDao = DoctorMemoryDao.getInstance();

    private Patient patient = new Patient();
    private String selectedEmail;
    private String doctorEmail;

    public List<Patient> getAllPatients() {
        return patientDao.getAll();
    }

    public String savePatient() {
        Doctor exDoc = patient.getPersonalDoctor();
        if (exDoc != null) {
            exDoc.removePatient(patient);
        }
        Doctor selectedDoctor = doctorDao.find(doctorEmail);
        patient.setPersonalDoctor(selectedDoctor);

        if (patient.getPersonalDoctor() != null) {
            selectedDoctor = doctorDao.find(patient.getPersonalDoctor().getEmail());
            selectedDoctor.addPatient(patient);
        }
        patientDao.save(patient);
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
        if (patient.getPersonalDoctor() != null) {
            Doctor selectedDoctor = doctorDao.find(patient.getPersonalDoctor().getEmail());
            selectedDoctor.removePatient(patient);
        }
    }

    public List<Patient> getPatientsWithNoDoctor() {
        List<Patient> allPatients = getAllPatients();
        return allPatients.stream().filter(p -> p.getPersonalDoctor() == null).collect(Collectors.toList());
    }
}
