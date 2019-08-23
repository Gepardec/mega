package connector.service.api;

import connector.rest.model.GoogleUser;
import de.provantis.zep.MitarbeiterType;
import de.provantis.zep.ReadMitarbeiterResponseType;

import javax.ws.rs.core.Response;
import java.util.List;

public interface WorkerService {
    ReadMitarbeiterResponseType login(GoogleUser user);
    Response updateWorker(List<MitarbeiterType> employees);
}