package com.gepardec.mega.rest.api;

import com.gepardec.mega.model.google.GoogleUser;
import de.provantis.zep.MitarbeiterType;
import de.provantis.zep.ReadMitarbeiterResponseType;
import org.jboss.resteasy.annotations.Body;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/worker")
public interface WorkerApi {

    @POST
    @Path("/employee")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response employee (final GoogleUser user, @Context HttpServletRequest request, @Context HttpServletResponse response);

    @POST
    @Path("/employees")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response employees(final GoogleUser user, @Context HttpServletRequest request, @Context HttpServletResponse response);

    @PUT
    @Path("/employees/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response employeesUpdate (List<MitarbeiterType> employees, @Context HttpServletRequest request, @Context HttpServletResponse response);

    @PUT
    @Path("/employee/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response employeesUpdate (MitarbeiterType employee, @Context HttpServletRequest request, @Context HttpServletResponse response);
}
