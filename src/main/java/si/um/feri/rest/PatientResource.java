package si.um.feri.rest;


import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import si.um.feri.dao.PatientDao;
import si.um.feri.dto.PatientDto;
import si.um.feri.vao.Patient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Path("/patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatientResource implements Serializable {
    private static final Logger log = Logger.getLogger("PatientResource");

    @EJB
    PatientDao patientDao;

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    @Path("/info")
    public String info() {
        return "PatientResource";
    }

    @GET
    public Response getAllPatients() {
        List<PatientDto> ret = new ArrayList<>();
        for (Patient p : patientDao.getAll())
            ret.add(new PatientDto(p));
        return Response.ok(ret).status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization, X-Requested-With")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD").build();
    }

    @GET
    @Path("/{email}")
    public Response getPatientByEmail(@PathParam("email") String email) {
        PatientDto p = new PatientDto(patientDao.find(email));
        return Response.ok(p).status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD").build();

    }

    @POST
    @Path("/{docEmail}")
    public void addPatient(Patient p, @PathParam("docEmail") String docEmail) throws Exception {
        patientDao.save(p, docEmail);
    }

    @OPTIONS
    @Path("/{email}/{docEmail}")
    public Response updatePatientPreFlight(@PathParam("email") String email, @PathParam("docEmail") String docEmail) throws Exception {
        if (patientDao.find(email) == null)
            throw new Exception("Patient does not exists.");
        Patient p = patientDao.find(email);
        patientDao.save(p, docEmail);
        return Response.ok(p).status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD").build();
    }

    @PUT
    @Path("/{email}/{docEmail}")
    public void updatePatient(@PathParam("email") String email, @PathParam("docEmail") String docEmail) throws Exception {
        if (patientDao.find(email) == null)
            throw new Exception("Patient does not exists.");
        Patient p = patientDao.find(email);
        patientDao.save(p, docEmail);
    }
}
