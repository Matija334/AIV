package si.um.feri;

import jakarta.ejb.Local;
import jakarta.ejb.Remote;
import jakarta.mail.MessagingException;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import javax.naming.NamingException;
import java.io.Serializable;

@Local
public interface SelectDoctor extends Serializable {
    void selectDoctor(Patient patient, Doctor doctor, boolean free) throws MessagingException, NamingException;

    void selectDoctorString(String patient, String doctor) throws MessagingException, NamingException;

    void test(String name);
}