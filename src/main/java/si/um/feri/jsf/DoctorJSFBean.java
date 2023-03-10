package si.um.feri.jsf;

import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import si.um.feri.dao.DoctorDao;
import si.um.feri.dao.DoctorDaoBean;
import si.um.feri.dao.PatientDao;
import si.um.feri.dao.PatientDaoBean;
import si.um.feri.vao.Doctor;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Named("doctors")
@ViewScoped
@Data
public class DoctorJSFBean implements Serializable {
    Logger log = Logger.getLogger(DoctorDaoBean.class.toString());

    @EJB
    private DoctorDao doctorDao;

    @EJB
    private PatientDao patientDao;


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
