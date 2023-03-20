package si.um.feri.dao;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.mail.MessagingException;
import si.um.feri.observers.NewDoctorObserver;
import si.um.feri.observers.OldDoctorObserver;
import si.um.feri.SelectDoctorBean;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import javax.naming.NamingException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class PatientDaoBean implements PatientDao, Serializable {

    private List<Patient> patientList = Collections.synchronizedList(new ArrayList<>());
    Logger log = Logger.getLogger(PatientDaoBean.class.toString());

    //Getting dao
    @EJB
    private DoctorDao doctorDao;

    /*
    public static PatientDaoBean instance = null;

    public static synchronized PatientDaoBean getInstance() {
        if (instance==null) instance=new PatientDaoBean();
        return instance;
    }
    */

    public PatientDaoBean(){
        patientList.add(new Patient("Gospod", "Debevec", "debevec@ehealth.com", LocalDate.parse("1975-01-01"), "Rabim Franjo!"));
        patientList.add(new Patient("Reševalec", "Mile", "resevalec@ehealth.com", LocalDate.parse("1983-05-13"), "Vozim rešilca!"));
        patientList.add(new Patient("Vratar", "Veso", "vratar@ehealth.com", LocalDate.parse("1950-06-22"), "Stražim vrata!"));
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
    public void save(Patient patient, String doctorEmail) throws MessagingException, NamingException {
        if(patient.getObserverList() != null) {
            patient.getObserverList().clear();
        }
        //Removing old personal doctor
        Doctor exDoc = patient.getPersonalDoctor();
        if (exDoc != null) {
            exDoc.removePatient(patient);
            patient.add(new OldDoctorObserver(exDoc, patient));
        }

        //Setting new personal doctor. Find by email
        Doctor selectedDoctor = doctorDao.find(doctorEmail);
        if(selectedDoctor != null) {
            boolean free = selectedDoctor.getPatientQuota() > selectedDoctor.getPatientList().size();
            patient = new SelectDoctorBean().selectDoctor(patient, selectedDoctor, free);
            patient.add(new NewDoctorObserver(selectedDoctor, patient));
        }

        //patient.setPersonalDoctor(selectedDoctor);

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

        patient.broadcast();

    }

    @Override
    public void delete(Patient patient) {
        patientList.remove(patient);

        //If patient had personal doctor, remove patient from patient list of doctor
        if (patient.getPersonalDoctor() != null) {
            Doctor selectedDoctor = doctorDao.find(patient.getPersonalDoctor().getEmail());
            selectedDoctor.removePatient(patient);
        }
    }
}
