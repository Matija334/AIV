package si.um.feri.rest;


import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import si.um.feri.dao.PatientDao;
import si.um.feri.dto.PatientDto;
import si.um.feri.vao.Patient;

import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;

@Path("/patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatientResource {
    private static final Logger log= Logger.getLogger("PatientResource");

    @EJB
    PatientDao patientDao;

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    @Path("/info")
    public String info() {
        return "PatientResource";
    }

    @GET
    public List<PatientDto> getAllPatients() {
        List<PatientDto> ret= new ArrayList<>();
        for (Patient p : patientDao.getAll())
            ret.add(new PatientDto(p));
        return ret;
    }

    @GET
    @Path("/{email}")
    public PatientDto getPatientByEmail(@PathParam("email") String email) {
        return new PatientDto(patientDao.find(email));
    }

    @POST
    public void addPatient(Patient p) throws Exception {
        patientDao.save(p, p.getDocEmail());
    }

    @PUT
    @Path("/{email}")
    public Patient updatePatient(Patient p,@PathParam("email") String email, String docEmail) throws Exception {
        if (patientDao.find(email)==null)
            throw new Exception("Patient does not exists.");
        log.info("ADFASDFASDFASDFASDFAS" + String.valueOf(p));
        patientDao.save(p, p.getDocEmail());
        return p;
    }
}
