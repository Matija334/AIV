package si.um.feri.strategy.concreteExamples;

import jakarta.mail.MessagingException;
import si.um.feri.EmailSender;
import si.um.feri.strategy.VisitInterface;
import si.um.feri.vao.Visit;

import javax.naming.NamingException;
import java.util.logging.Logger;

public class PrescriptionsStrategy implements VisitInterface {
    Logger log=Logger.getLogger(PrescriptionsStrategy.class.toString());
    @Override
    public void endVisit(Visit visit) throws MessagingException, NamingException {
        log.info("Rabis tablete");
        EmailSender.send(visit.getPatient().getEmail(), "Postavljena diagnoza", "Doktor: " + visit.getDoctor().getName() + " " + visit.getDoctor().getLastName() + " vam je postavil diagnozo: " + visit.getDetails());
        EmailSender.send(visit.getPatient().getEmail(), "Predpisana zdravila", "Doktor: " + visit.getDoctor().getName() + " " + visit.getDoctor().getLastName() + " vam je predpisal zdravila: " + visit.getPrescriptions());
    }
}
