package si.um.feri.dao;

import jakarta.mail.MessagingException;
import si.um.feri.vao.Patient;

import javax.naming.NamingException;
import java.util.List;

public interface PatientDao {
    List<Patient> getAll();

    Patient find(String email);

    void save(Patient patient, String doctorEmail) throws MessagingException, NamingException;

    void delete(Patient patient);
}
