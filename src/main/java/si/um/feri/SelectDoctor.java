package si.um.feri;

import jakarta.ejb.Remote;
import jakarta.mail.MessagingException;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import javax.naming.NamingException;
import java.io.Serializable;

@Remote
public interface SelectDoctor extends Serializable {
    Patient selectDoctor(Patient patient, Doctor doctor, boolean free) throws MessagingException, NamingException;

    void selectDoctorString(String patient, String doctor) throws MessagingException, NamingException;

    void test(String name);
}