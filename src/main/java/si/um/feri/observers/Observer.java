package si.um.feri.observers;

import jakarta.mail.MessagingException;

import javax.naming.NamingException;

public interface Observer {
    void action() throws MessagingException, NamingException;
}
