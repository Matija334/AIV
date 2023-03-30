package si.um.feri.vao;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private Doctor doctor  = new Doctor();
    @ManyToOne
    private Patient patient = new Patient();
    private LocalDateTime time;
    private String details;
    private String prescriptions;
    private boolean ended;

    public Visit() {
    }

    public Visit(Doctor doctor, Patient patient, LocalDateTime time, String details, String prescriptions, boolean ended) {
        this.doctor = doctor;
        this.patient = patient;
        this.time = time;
        this.details = details;
        this.prescriptions = prescriptions;
        this.ended = ended;
    }

    @Override
    public String toString() {
        return doctor.getEmail() + "," + patient.getEmail() + "," + time + "," + details + "," + prescriptions + "," + ended;
    }
}
