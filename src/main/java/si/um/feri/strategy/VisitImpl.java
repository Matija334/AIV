package si.um.feri.strategy;

import jakarta.mail.MessagingException;
import si.um.feri.dao.DoctorDaoBean;
import si.um.feri.strategy.concreteExamples.DiagnosisAndPrescriptionStrategy;
import si.um.feri.strategy.concreteExamples.DiagnosisStrategy;
import si.um.feri.strategy.concreteExamples.NoSpecialtiesStrategy;
import si.um.feri.strategy.concreteExamples.PrescriptionsStrategy;
import si.um.feri.vao.Visit;

import javax.naming.NamingException;
import java.util.Objects;
import java.util.logging.Logger;

public class VisitImpl {
    VisitInterface visitInterface;
    Logger log = Logger.getLogger(VisitImpl.class.toString());

    private void treatmentType(Visit visit) {
        if(!Objects.equals(visit.getPrescriptions(), "") && !Objects.equals(visit.getDetails(), "")){
            visitInterface = new PrescriptionsStrategy();
            return;
        }
        if(!Objects.equals(visit.getDetails(), "")){
            visitInterface = new DiagnosisStrategy();
            return;
        }

        visitInterface = new NoSpecialtiesStrategy();
    }

    public void visit(Visit visit) throws MessagingException, NamingException {
        treatmentType(visit);
        visitInterface.endVisit(visit);
    }
}
