package si.um.feri.dao;

import jakarta.ejb.Local;
import si.um.feri.vao.Doctor;

import java.util.List;

@Local
public interface DoctorDao {
    List<Doctor> getAll();

    Doctor find(String email);

    void save(Doctor doctor);

    void delete(Doctor doctor);
}
