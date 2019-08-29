package connector.rest.impl;

import connector.rest.api.WorkerApi;
import connector.rest.model.GoogleUser;
import connector.service.api.WorkerService;
import de.provantis.zep.MitarbeiterType;
import de.provantis.zep.ReadMitarbeiterResponseType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
public class WorkerImpl implements WorkerApi {

    @Inject
    private WorkerService workerService;

    @Override
    public String hello() {
        return "hello";
    }

    @Override
    public ReadMitarbeiterResponseType login(GoogleUser user) {
        return workerService.login(user);
    }

    @Override
    public Response updateWorker(List<MitarbeiterType> employees) {
        return workerService.updateWorker(employees);
    }
}
