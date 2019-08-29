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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
public class WorkerImpl implements WorkerApi {

    @Inject
    WorkerService workerService;

    @Override
    public MitarbeiterType get(GoogleUser user, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        return workerService.get(user);
    }


    @Override
    @Authorization(allowedRoles = {SessionUser.ROLE_ADMINISTRATOR, SessionUser.ROLE_CONTROLLER})
    public ReadMitarbeiterResponseType getAll(GoogleUser user, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        return workerService.getAll(user);
    }

    @Override
    @Authorization(allowedRoles = {SessionUser.ROLE_ADMINISTRATOR, SessionUser.ROLE_CONTROLLER})
    public Response updateWorker(List<MitarbeiterType> employees, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        return workerService.updateWorker(employees);
    }
}
