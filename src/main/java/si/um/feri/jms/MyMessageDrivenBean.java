package si.um.feri.jms;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import si.um.feri.dao.DoctorDao;
import si.um.feri.dao.PatientDao;

import java.util.logging.Logger;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/test"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class MyMessageDrivenBean implements MessageListener {

    @EJB
    private PatientDao patientDao;

    Logger log=Logger.getLogger(MyMessageDrivenBean.class.toString());
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage tm = (TextMessage) message;
            try {
                log.info("SPOROCILO: "+tm.getText());
                String info = tm.getText();
                String[] emails = info.split(",");
                String doctorEmail = emails[0];
                String patientEmail = emails[1];
                patientDao.save(patientDao.find(patientEmail), doctorEmail);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("Prispelo je neznano sporocilo.");
        }
    }
}
