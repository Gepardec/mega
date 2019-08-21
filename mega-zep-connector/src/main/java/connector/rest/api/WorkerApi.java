package connector.rest.api;

import connector.rest.model.GoogleUser;
import de.provantis.zep.ReadMitarbeiterResponseType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/worker")
public interface WorkerApi {
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    String hello();

    @POST
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    ReadMitarbeiterResponseType login(GoogleUser user);
}
