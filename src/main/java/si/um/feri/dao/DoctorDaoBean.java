package si.um.feri.dao;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import si.um.feri.EmailSender;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class DoctorDaoBean implements DoctorDao, Serializable {
    @EJB
    private PatientDao patientDao;
    private final List<Doctor> doctorList = Collections.synchronizedList(new ArrayList<>());
    Logger log = Logger.getLogger(DoctorDaoBean.class.toString());
    /*
    public static DoctorDaoBean instance = null;

    public static synchronized DoctorDaoBean getInstance() {
        if (instance==null) instance=new DoctorDaoBean();
        return instance;
    }*/

    public DoctorDaoBean(){
        doctorList.add(new Doctor("Doktor", "Jarc", "jarc@ehealth.com",10));
        doctorList.add(new Doctor("Doktor", "Muc", "muc@ehealth.com",5));
        doctorList.add(new Doctor("Doktor", "Ščinkavc", "scinkavc@ehealth.com",3));
    }

    @Override
    public List<Doctor> getAll() {
        log.info("DAO: get all");
        return doctorList;
    }

    @Override
    public Doctor find(String email) {
        log.info("DAO: finding " + email);
        for (Doctor doctor : doctorList)
            if (doctor.getEmail().equals(email))
                return doctor;
        return null;
    }

    @Override
    public void save(Doctor doctor) {
        log.info("DAO: saving " + doctor);

        //If doctor is being edited, first delete
        if (find(doctor.getEmail()) != null) {
            log.info("DAO: editing " + doctor);
            delete(doctor);
        }

        //Adding new doctor
        doctorList.add(doctor);

        //Add 'new' doctor to all patients that were on doctor's list
        if (doctor.getPatientList().size() != 0) {
            for (Patient patient : doctor.getPatientList()) {
                Patient selectedPatient = patientDao.find(patient.getEmail());
                selectedPatient.setPersonalDoctor(doctor);
            }
        }
    }

    @Override
    public void delete(Doctor doctor) {
        List<Patient> patientList = doctor.getPatientList();
        for (Patient p : patientList)
            p.setPersonalDoctor(null);
        doctorList.remove(doctor);
    }
}
