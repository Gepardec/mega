package connector.rest.impl;

import connector.rest.api.WorkerApi;
import connector.rest.model.GoogleUser;
import connector.service.api.WorkerService;
import de.provantis.zep.ReadMitarbeiterResponseType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
}
