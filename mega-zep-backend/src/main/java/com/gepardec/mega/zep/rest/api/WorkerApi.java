package com.gepardec.mega.zep.rest.api;

import com.gepardec.mega.model.google.GoogleUser;
import de.provantis.zep.MitarbeiterType;
import de.provantis.zep.ReadMitarbeiterResponseType;

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
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response status();

    @POST
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    MitarbeiterType get (GoogleUser user, @Context HttpServletRequest request, @Context HttpServletResponse response);

    @POST
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    ReadMitarbeiterResponseType getAll (GoogleUser user, @Context HttpServletRequest request, @Context HttpServletResponse response);

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateWorker (List<MitarbeiterType> employees, @Context HttpServletRequest request, @Context HttpServletResponse response);
}
