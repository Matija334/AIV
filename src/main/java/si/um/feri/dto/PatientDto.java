package si.um.feri.dto;

import lombok.Data;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import java.time.LocalDate;

@Data
public class PatientDto {

    public PatientDto(Patient patient){
        this.name = patient.getName();
        this.lastName = patient.getLastName();
        this.email = patient.getEmail();
        this.birthday = patient.getBirthday();
        this.info = patient.getInfo();
        if(patient.getPersonalDoctor() == null) {
            this.name_doctor = "";
            this.lastName_doctor = "";
            this.email_doctor = "";
            this.patientQuota_doctor = 0;
        } else {
            this.name_doctor = patient.getPersonalDoctor().getName();
            this.lastName_doctor = patient.getPersonalDoctor().getLastName();
            this.email_doctor = patient.getPersonalDoctor().getEmail();
            this.patientQuota_doctor = patient.getPersonalDoctor().getPatientQuota();
        }
    }
    private String name_doctor;
    private String lastName_doctor;
    private String email_doctor;
    private int patientQuota_doctor;


    private String name;
    private String lastName;
    private String email;
    private LocalDate birthday;
    private String info;
}
