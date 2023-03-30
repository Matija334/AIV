package si.um.feri.strategy.concreteExamples;

import si.um.feri.strategy.VisitInterface;
import si.um.feri.vao.Visit;

import java.util.logging.Logger;

public class NoSpecialtiesStrategy implements VisitInterface {
    Logger log=Logger.getLogger(NoSpecialtiesStrategy.class.toString());
    @Override
    public void endVisit(Visit visit) {
        log.info("Nic ti ni");
    }
}
