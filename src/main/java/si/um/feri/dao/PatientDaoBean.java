package si.um.feri.dao;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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

    @PersistenceContext
    EntityManager em;

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
    }

    @PostConstruct
    public void init() {
        Patient patient1 = new Patient("Gospod", "Debevec", "debevec@ehealth.com", LocalDate.parse("1975-01-01"), "Rabim Franjo!");
        Patient patient2 = new Patient("Reševalec", "Mile", "resevalec@ehealth.com", LocalDate.parse("1983-05-13"), "Vozim rešilca!");
        Patient patient3 = new Patient("Vratar", "Veso", "vratar@ehealth.com", LocalDate.parse("1950-06-22"), "Stražim vrata!");

        em.persist(patient1);
        em.persist(patient2);
        em.persist(patient3);
    }


    @Override
    public List<Patient> getAll() {
        log.info("DAO: get all");
        return em.createQuery("select p from Patient p", Patient.class).getResultList();
    }

    @Override
    public Patient find(String email) {
        log.info("DAO: finding " + email);
        TypedQuery<Patient> query = em.createQuery("SELECT p FROM Patient p WHERE p.email = :email", Patient.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
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
        boolean free = false;
        if(selectedDoctor != null) {
            free = selectedDoctor.getPatientQuota() > selectedDoctor.getPatientList().size();
        }

        log.info("DAO: saving " + patient);

        //If patient is being edited
        Patient existingPatient = find(patient.getEmail());
        if (existingPatient != null) {
            log.info("DAO: editing " + existingPatient);
            existingPatient.setName(patient.getName());
            existingPatient.setLastName(patient.getLastName());
            existingPatient.setEmail(patient.getEmail());
            existingPatient.setBirthday(patient.getBirthday());
            existingPatient.setInfo(patient.getInfo());
            if(selectedDoctor != null) {
                new SelectDoctorBean().selectDoctor(existingPatient, selectedDoctor, free);
                existingPatient.add(new NewDoctorObserver(selectedDoctor, patient));
            } else {
                existingPatient.setPersonalDoctor(null);
            }
            em.merge(existingPatient);
        } else {
            new SelectDoctorBean().selectDoctor(patient, selectedDoctor, free);
            patient.add(new NewDoctorObserver(selectedDoctor, patient));
            em.persist(patient);
        }

        patient.broadcast();
    }

    @Override
    public void delete(Patient patient) {
        // If patient had personal doctor, remove patient from patient list of doctor
        Doctor personalDoctor = patient.getPersonalDoctor();
        if (personalDoctor != null) {
            personalDoctor.removePatient(patient);
            patient.setPersonalDoctor(null);
            em.merge(personalDoctor);
        }
        // Remove patient entity
        Patient managedPatient = em.merge(patient);
        em.flush();
        em.remove(managedPatient);
    }

}
