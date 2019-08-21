package connector.service.api;

import connector.rest.model.GoogleUser;
import de.provantis.zep.ReadMitarbeiterResponseType;

public interface WorkerService {
    ReadMitarbeiterResponseType login(GoogleUser user);
}
