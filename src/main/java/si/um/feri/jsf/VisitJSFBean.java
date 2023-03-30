package si.um.feri.jsf;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.mail.MessagingException;
import lombok.Data;
import si.um.feri.dao.DoctorDao;
import si.um.feri.dao.DoctorDaoBean;
import si.um.feri.dao.PatientDao;
import si.um.feri.dao.VisitDao;
import si.um.feri.strategy.concreteExamples.DiagnosisStrategy;
import si.um.feri.vao.Visit;
import si.um.feri.strategy.VisitImpl;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import javax.naming.NamingException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named("visits")
@ViewScoped
@Data
public class VisitJSFBean implements Serializable {

    Logger log = Logger.getLogger(VisitJSFBean.class.toString());
    @EJB
    private DoctorDao doctorDao;
    @EJB
    private PatientDao patientDao;
    @EJB
    private VisitDao visitDao;
    private Visit visit = new Visit();
    private String doctorEmail;
    private String patientEmail;
    private Doctor doctor = new Doctor();
    private Patient patient = new Patient();
    private String details;
    private String prescriptions;
    private long id;

    public List<Visit> getAllVisits() {
        return visitDao.getAll();
    }

    public String saveVisit(){
        if(visitDao.find(visit.getId()) == null){
            patient = patientDao.find(patientEmail);
            doctor = doctorDao.find(doctorEmail);
            visit.setPatient(patient);
            visit.setDoctor(doctor);
        }
        visit.setTime(LocalDateTime.now());
        visitDao.save(visit);
        return "a";
    }

    public String endVisit() throws MessagingException, NamingException {
        if(visitDao.find(visit.getId()) == null){
            patient = patientDao.find(patientEmail);
            doctor = doctorDao.find(doctorEmail);
            visit.setPatient(patient);
            visit.setDoctor(doctor);
        }
        visit.setEnded(true);
        VisitImpl v = new VisitImpl();
        v.visit(visit);
        saveVisit();
        return "a";
    }

    public void setId(long id) {
        this.id = id;
        visit = visitDao.find(id);
        if (visit == null){
            visit = new Visit();
        }
    }

    public List<Visit> getCurrentVisits() {
        List<Visit> allVisits = getAllVisits();
        return allVisits.stream().filter(v -> !v.isEnded()).collect(Collectors.toList());
    }
    public List<Visit> getEndedVisits() {
        List<Visit> allVisits = getAllVisits();
        return allVisits.stream().filter(Visit::isEnded).collect(Collectors.toList());
    }
}
