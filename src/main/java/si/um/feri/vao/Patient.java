package si.um.feri.vao;

import jakarta.mail.MessagingException;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import si.um.feri.observers.Observable;
import si.um.feri.observers.Observer;

import javax.naming.NamingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class Patient implements Observable {
    @NotBlank
    private String name;
    private String lastName;
    private String email;
    private LocalDate birthday;
    private String info;
    private Doctor personalDoctor;

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
