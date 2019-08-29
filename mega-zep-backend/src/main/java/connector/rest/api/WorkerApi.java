package connector.rest.api;

import connector.rest.model.GoogleUser;
import de.provantis.zep.MitarbeiterType;
import de.provantis.zep.ReadMitarbeiterResponseType;
import org.apache.http.HttpStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/worker")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface WorkerApi {

    @GET
    @Path("/status")
    Response status();

    @GET
    @Path("/get")

    String hello();

    @POST
    @Path("/get")
    MitarbeiterType get(GoogleUser user);

    @POST
    @Path("/getAll")
    ReadMitarbeiterResponseType getAll(GoogleUser user);

    @PUT
    @Path("/update")
    Response updateWorker(List<MitarbeiterType> employees);
}
