package si.um.feri.jsf;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import si.um.feri.dao.DoctorMemoryDao;
import si.um.feri.dao.PatientMemoryDao;
import si.um.feri.vao.Doctor;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Named("doctors")
@ViewScoped
@Data
public class DoctorJSFBean implements Serializable {
    Logger log = Logger.getLogger(DoctorMemoryDao.class.toString());

    private DoctorMemoryDao doctorDao = DoctorMemoryDao.getInstance();

    private PatientMemoryDao patientDao = PatientMemoryDao.getInstance();


    private Doctor doctor = new Doctor();

    private String selectedEmail;

    public List<Doctor> getAllDoctors() {
        return doctorDao.getAll();
    }

    public String saveDoctor() {
        doctorDao.save(doctor);
        return "all";
    }

    public void setSelectedEmail(String email) {
        selectedEmail = email;
        doctor = doctorDao.find(email);
        if (doctor == null) doctor = new Doctor();
    }

    public void delete(Doctor doctor) {
        doctorDao.delete(doctor);
    }
}
