package si.um.feri.strategy;

import jakarta.mail.MessagingException;
import si.um.feri.vao.Visit;

import javax.naming.NamingException;

public interface VisitInterface {
    public void endVisit(Visit visit) throws MessagingException, NamingException;
}
