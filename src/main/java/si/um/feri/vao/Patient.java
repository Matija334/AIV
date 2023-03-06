package si.um.feri.vao;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Patient {
    @NotBlank
    private String name;
    private String lastName;
    private String email;
    private LocalDate birthday;
    private String info;
    private Doctor personalDoctor;
    public Patient() {
    }
    public Patient(String name, String lastName, String email, LocalDate birthday, String info) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.info = info;
        this.personalDoctor = null;
    }

    @Override
    public String toString() {
        return name + "," + lastName + "," + email + "," + birthday + "," + info;
    }
}
