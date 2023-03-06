package si.um.feri.dao;

import si.um.feri.vao.Patient;

import java.util.List;

public interface PatientDao {
    List<Patient> getAll();

    Patient find(String email);

    void save(Patient patient, String doctorEmail);

    void delete(Patient patient);
}
