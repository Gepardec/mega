package connector.rest.impl;

import connector.annotations.Authorization;
import connector.rest.api.WorkerApi;
import connector.rest.model.GoogleUser;
import connector.security.SessionUser;
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
    WorkerService workerService;

    @Override
    public String hello() {
        return "hello";
    }

    @Override
    public MitarbeiterType get(GoogleUser user) {
        return workerService.get(user);
    }


    @Override
    @Authorization(allowedRoles = {SessionUser.ROLE_ADMINISTRATOR, SessionUser.ROLE_CONTROLLER})
    public ReadMitarbeiterResponseType getAll(GoogleUser user) {
        return workerService.getAll(user);
    }

    @Override
    @Authorization(allowedRoles = {SessionUser.ROLE_ADMINISTRATOR, SessionUser.ROLE_CONTROLLER})
    public Response updateWorker(List<MitarbeiterType> employees) {
        return workerService.updateWorker(employees);
    }
}
