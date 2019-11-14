package com.gepardec.mega.rest.api;

import com.gepardec.mega.model.google.GoogleUser;
import de.provantis.zep.MitarbeiterType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/worker")
public interface WorkerApi {

    @OPTIONS
    @Path("/employee")
    @Produces(MediaType.APPLICATION_JSON)
    Response employee (@Context HttpServletRequest request, @Context HttpServletResponse response);

    @POST
    @Path("/employee")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response employee (final GoogleUser user, @Context HttpServletRequest request, @Context HttpServletResponse response);

    @OPTIONS
    @Path("/employees")
    @Produces(MediaType.APPLICATION_JSON)
    Response employees (@Context HttpServletRequest request, @Context HttpServletResponse response);

    @POST
    @Path("/employees")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response employees (final GoogleUser user, @Context HttpServletRequest request, @Context HttpServletResponse response);

    @OPTIONS
    @Path("/employees/update")
    @Produces(MediaType.APPLICATION_JSON)
    Response employeesUpdate (@Context HttpServletRequest request, @Context HttpServletResponse response);

    @PUT
    @Path("/employees/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response employeesUpdate (List<MitarbeiterType> employees, @Context HttpServletRequest request, @Context HttpServletResponse response);

    @OPTIONS
    @Path("/employee/update")
    @Produces(MediaType.APPLICATION_JSON)
    Response employeeUpdate (@Context HttpServletRequest request, @Context HttpServletResponse response);

    @PUT
    @Path("/employee/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response employeeUpdate (MitarbeiterType employee, @Context HttpServletRequest request, @Context HttpServletResponse response);
}
