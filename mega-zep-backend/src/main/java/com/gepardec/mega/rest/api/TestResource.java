package com.gepardec.mega.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/test/")
public interface TestResource {
    @Path("/sync/projects")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response syncProjects();

    @Path("/sync/enterprise")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response syncEnterpriseEntries();

    @Path("/sync")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response synctest();

    @Path("/mail-comment")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response mailComment();
}
