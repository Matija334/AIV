package si.um.feri.dao;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import si.um.feri.EmailSender;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;
import si.um.feri.vao.Visit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class DoctorDaoBean implements DoctorDao, Serializable {

    @PersistenceContext
    EntityManager em;

    @EJB
    private PatientDao patientDao;

    Logger log = Logger.getLogger(DoctorDaoBean.class.toString());

    @PostConstruct
    public void init() {
        Doctor doctor1 = new Doctor("Doktor", "Jarc", "jarc@ehealth.com",10);
        Doctor doctor2 = new Doctor("Doktor", "Muc", "muc@ehealth.com",5);
        Doctor doctor3 = new Doctor("Doktor", "Ščinkavc", "scinkavc@ehealth.com",3);

        em.persist(doctor1);
        em.persist(doctor2);
        em.persist(doctor3);
    }

    @Override
    public List<Doctor> getAll() {
        log.info("DAO: get all");
        return em.createQuery("select d from Doctor d", Doctor.class).getResultList();
    }

    @Override
    public Doctor find(String email) {
        log.info("DAO: finding " + email);
        TypedQuery<Doctor> query = em.createQuery("SELECT d FROM Doctor d WHERE d.email = :email", Doctor.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    @Override
    public void save(Doctor doctor) {
        log.info("DAO: saving " + doctor);

        //If doctor is being edited, first delete
        Doctor existingDoctor = find(doctor.getEmail());
        if (existingDoctor != null) {
            log.info("DAO: editing " + doctor);
            existingDoctor.setName(doctor.getName());
            existingDoctor.setLastName(doctor.getLastName());
            existingDoctor.setEmail(doctor.getEmail());
            existingDoctor.setPatientQuota(doctor.getPatientQuota());
            existingDoctor.setPatientList(doctor.getPatientList());
            em.merge(existingDoctor);
        } else {
            //Adding new doctor
            em.persist(doctor);

            //Add 'new' doctor to all patients that were on doctor's list
            if (doctor.getPatientList().size() != 0) {
                for (Patient patient : doctor.getPatientList()) {
                    Patient selectedPatient = patientDao.find(patient.getEmail());
                    selectedPatient.setPersonalDoctor(doctor);
                }
            }
        }
    }


    @Override
    public void delete(Doctor doctor) {
        List<Patient> patientList = doctor.getPatientList();
        for (Patient p : patientList)
            p.setPersonalDoctor(null);
        TypedQuery<Visit> query= em.createQuery("select v from Visit v WHERE v.doctor.email = :email ", Visit.class);
        query.setParameter("email", doctor.getEmail());
        List<Visit> visits = query.getResultList();
        for (Visit v : visits)
            v.setDoctor(null);

        Doctor managedDoctor = em.merge(doctor);
        em.remove(managedDoctor);
    }
}
