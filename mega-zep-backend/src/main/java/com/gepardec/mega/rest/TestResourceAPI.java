package com.gepardec.mega.rest;

import io.quarkus.arc.properties.IfBuildProperty;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@IfBuildProperty(name = "mega.endpoint.test.enable", stringValue = "true", enableIfMissing = true)
@RequestScoped
@Path("/test/")
public interface TestResourceAPI {
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
