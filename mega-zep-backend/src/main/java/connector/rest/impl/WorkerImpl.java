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
    WorkerService workerService;

    @Override
    public String hello() {
        return "hello";
    }

    @Override
    public MitarbeiterType get(GoogleUser user) {
        System.out.println(user.getFirstName());
        System.out.println(user.getLastName());
        System.out.println(user.getEmail());

        return workerService.get(user);
    }


    @Override
    public ReadMitarbeiterResponseType getAll(GoogleUser user) {
        System.out.println(user.getFirstName());
        System.out.println(user.getLastName());
        System.out.println(user.getEmail());
        return workerService.getAll(user);
    }

    @Override
    public Response updateWorker(List<MitarbeiterType> employees) {
        return workerService.updateWorker(employees);
    }
}
