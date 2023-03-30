package si.um.feri.dao;

import jakarta.ejb.Local;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;
import si.um.feri.vao.Visit;

import java.util.List;

@Local
public interface VisitDao {
    List<Visit> getAll();
    Visit find(long id);
    void save(Visit visit);
}
