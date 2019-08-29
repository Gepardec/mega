package com.gepardec.mega.backend.rest.impl;

import com.gepardec.mega.backend.rest.api.WorkerApi;
import com.gepardec.mega.backend.rest.model.GoogleUser;
import com.gepardec.mega.backend.service.api.WorkerService;
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
