package si.um.feri.dao;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import si.um.feri.vao.Visit;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class VisitDaoBean implements VisitDao, Serializable {
    @PersistenceContext
    EntityManager em;

    @EJB
    private PatientDao patientDao;

    Logger log = Logger.getLogger(VisitDaoBean.class.toString());

    @Override
    public List<Visit> getAll() {
        log.info("DAO: get all");
        return em.createQuery("select v from Visit v", Visit.class).getResultList();
    }

    @Override
    public Visit find(long id) {
        log.info("DAO: finding " + id);
        TypedQuery<Visit> query = em.createQuery("SELECT v FROM Visit v WHERE v.id = :id", Visit.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Visit visit) {
        Visit existingVisit = find(visit.getId());
        if(existingVisit != null){
            em.merge(visit);
        } else {
            em.persist(visit);
        }
    }
}
