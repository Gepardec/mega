package com.gepardec.mega.rest;

import com.gepardec.mega.service.api.init.StepEntrySyncService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/synctest")
public class SyncTestResource {

    @Inject
    StepEntrySyncService stepEntrySyncService;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response synctest() {
        stepEntrySyncService.genereteStepEntries();
        return Response.ok("ok").build();
    }
}
