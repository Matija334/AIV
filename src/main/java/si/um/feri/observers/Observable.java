package si.um.feri.observers;

import jakarta.mail.MessagingException;

import javax.naming.NamingException;

public interface Observable {
    void add(Observer o);
    void remove(Observer o);
    void broadcast() throws MessagingException, NamingException;
}
