package si.um.feri.vao;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Entity
public class Doctor implements Serializable {
    @JsonbTransient
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank
    private String name;
    private String lastName;
    private String email;
    private int patientQuota;

    @OneToMany(mappedBy = "personalDoctor", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Patient> patientList = Collections.synchronizedList(new ArrayList<>());

    public Doctor() {
    }

    public Doctor(String name, String lastName, String email, int patientQuota) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.patientQuota = patientQuota;
    }

    public void addPatient(Patient patient) {
        patientList.add(patient);
    }

    public void removePatient(Patient patient) {
        patientList.remove(patient);
    }

    @Override
    public String toString() {
        return name + "," + lastName + "," + email + "," + patientQuota;
    }
}
