package si.um.feri.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import si.um.feri.dao.DoctorDao;
import si.um.feri.dao.PatientDao;
import si.um.feri.dto.PatientDto;
import si.um.feri.vao.Doctor;
import si.um.feri.vao.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Path("/doctors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DoctorResource {

    private static final Logger log= Logger.getLogger("PatientResource");

    @EJB
    DoctorDao doctorDao;

    @GET
    public Response getAllDoctors() {
        return Response.ok(doctorDao.getAll()).status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD").build();
    }
}
