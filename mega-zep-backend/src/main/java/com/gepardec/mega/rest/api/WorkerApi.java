package com.gepardec.mega.rest.api;

import com.gepardec.mega.model.google.GoogleUser;
import de.provantis.zep.MitarbeiterType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/worker")
public interface WorkerApi {


    @POST
    @Path("/employee")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response employee(final GoogleUser user);

    @POST
    @Path("/employee/monthendReport")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response employeeMonthendReport(final GoogleUser user);


    @POST
    @Path("/employees")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response employees();


    @PUT
    @Path("/employees/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response employeesUpdate(List<MitarbeiterType> employees);

    @PUT
    @Path("/employee/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response employeeUpdate(MitarbeiterType employee);
}