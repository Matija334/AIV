package si.um.feri.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import javax.print.Doc;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class PatientMemoryDao implements PatientDao, Serializable {

    private List<Patient> patientList = Collections.synchronizedList(new ArrayList<>());
    Logger log = Logger.getLogger(PatientMemoryDao.class.toString());

    public static PatientMemoryDao instance = null;

    public static synchronized PatientMemoryDao getInstance() {
        if (instance==null) instance=new PatientMemoryDao();
        return instance;
    }

    private PatientMemoryDao(){
        patientList.add(new Patient("Gospod", "Debevec", "pacient@nmk.si", LocalDate.parse("1975-01-01"), "Rabim Franjo!"));
        patientList.add(new Patient("Reševalec", "Mile", "resevalec@nmk.si", LocalDate.parse("1983-05-13"), "Vozim rešilca!"));
        patientList.add(new Patient("Vratar", "Veso", "vratar@nmk.si", LocalDate.parse("1950-06-22"), "Stražim vrata!"));
    }


    @Override
    public List<Patient> getAll() {
        log.info("DAO: get all");
        return patientList;
    }

    @Override
    public Patient find(String email) {
        log.info("DAO: finding " + email);
        for (Patient patient : patientList) {
            if (patient.getEmail().equals(email))
                return patient;
        }
        return null;
    }

    @Override
    public void save(Patient patient, String doctorEmail) {
        //Removing old personal doctor
        Doctor exDoc = patient.getPersonalDoctor();
        if (exDoc != null)
            exDoc.removePatient(patient);

        //Getting dao
        DoctorMemoryDao doctorDao = DoctorMemoryDao.getInstance();

        //Setting new personal doctor. Find by email
        Doctor selectedDoctor = doctorDao.find(doctorEmail);
        patient.setPersonalDoctor(selectedDoctor);

        log.info("DAO: saving " + patient);

        //If patient is being edited, first delete
        if (find(patient.getEmail()) != null) {
            log.info("DAO: editing " + patient);
            delete(patient);
        }

        //Adding new patient
        patientList.add(patient);

        //If patient selected personal doctor, add patient to patient list of doctor
        if (patient.getPersonalDoctor() != null) {
            selectedDoctor = doctorDao.find(patient.getPersonalDoctor().getEmail());
            selectedDoctor.addPatient(patient);
        }

    }

    @Override
    public void delete(Patient patient) {
        DoctorMemoryDao doctorDao = DoctorMemoryDao.getInstance();
        patientList.remove(patient);

        //If patient had personal doctor, remove patient from patient list of doctor
        if (patient.getPersonalDoctor() != null) {
            Doctor selectedDoctor = doctorDao.find(patient.getPersonalDoctor().getEmail());
            selectedDoctor.removePatient(patient);
        }
    }
}
