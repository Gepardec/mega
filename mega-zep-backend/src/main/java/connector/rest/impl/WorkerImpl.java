package connector.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import connector.rest.api.WorkerApi;
import connector.rest.model.GoogleUser;
import connector.service.api.WorkerService;
import de.provantis.zep.MitarbeiterType;
import de.provantis.zep.ReadMitarbeiterResponseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.HttpStatus;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@ApplicationScoped
public class WorkerImpl implements WorkerApi {

    @Inject
    WorkerService workerService;

    @Override
    public Response status () {
        final ServiceStatus serviceStatus = new ServiceStatus(HttpStatus.SC_OK, "OK");
        final ObjectMapper objectMapper = new ObjectMapper();
        final StringWriter stringWriter = new StringWriter();
        try {
            objectMapper.writeValue(stringWriter, serviceStatus);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Response.status(HttpStatus.SC_OK).entity(stringWriter.toString()).build();
    }

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

    @AllArgsConstructor
    @Data
    private class ServiceStatus {
        private final int code;
        private final String message;
    }
}
