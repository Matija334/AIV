package si.um.feri.vao;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.mail.MessagingException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import si.um.feri.observers.Observable;
import si.um.feri.observers.Observer;

import javax.naming.NamingException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Entity
public class Patient implements Observable, Serializable {
    @JsonbTransient
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank
    private String name;
    private String lastName;
    private String email;
    private LocalDate birthday;
    private String info;
    @Transient
    private String docEmail;

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Doctor personalDoctor;
    @JsonbTransient
    @Transient
    private List<Observer> observerList = Collections.synchronizedList(new ArrayList<>());
    public Patient() {
    }
    public Patient(String name, String lastName, String email, LocalDate birthday, String info) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.info = info;
        this.personalDoctor = null;
        this.docEmail = "";
    }

    @Override
    public void add(Observer o) {
        observerList.add(o);
    }

    @Override
    public void remove(Observer o) {
        observerList.remove(o);
    }

    @Override
    public void broadcast() throws MessagingException, NamingException {
        for(Observer o: observerList){
            o.action();
        }
    }

    @Override
    public String toString() {
        return name + "," + lastName + "," + email + "," + birthday + "," + info;
    }
}
