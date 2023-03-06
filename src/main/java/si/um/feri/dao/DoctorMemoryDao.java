package si.um.feri.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class DoctorMemoryDao implements DoctorDao, Serializable {
    private PatientMemoryDao patientDao = PatientMemoryDao.getInstance();
    private final List<Doctor> doctorList = Collections.synchronizedList(new ArrayList<>());
    Logger log = Logger.getLogger(DoctorMemoryDao.class.toString());

    public static DoctorMemoryDao instance = null;

    public static synchronized DoctorMemoryDao getInstance() {
        if (instance==null) instance=new DoctorMemoryDao();
        return instance;
    }

    private DoctorMemoryDao(){
        doctorList.add(new Doctor("Doktor", "Jarc", "doktor.jarc@nmk.si",10));
        doctorList.add(new Doctor("Doktor", "Muc", "doktor.muc@nmk.si",5));
        doctorList.add(new Doctor("Doktor", "Ščinkavc", "doktor.scinkavc@nmk.si",3));
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
