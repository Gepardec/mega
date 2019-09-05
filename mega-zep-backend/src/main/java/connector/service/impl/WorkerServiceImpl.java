package connector.service.impl;

import connector.rest.model.GoogleUser;
import connector.security.AuthorizationInterceptor;
import connector.service.api.WorkerService;
import de.provantis.zep.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.core.Response;
import java.util.List;

@Interceptors(AuthorizationInterceptor.class)
@RequestScoped
public class WorkerServiceImpl implements WorkerService {

    @Inject
    ZepSoapPortType zepSoapPortType;

    @Inject
    RequestHeaderType requestHeaderType;

    @Override
    public ReadMitarbeiterResponseType getAll(GoogleUser user) {
        ReadMitarbeiterRequestType empl = new ReadMitarbeiterRequestType();
        empl.setRequestHeader(requestHeaderType);

        return zepSoapPortType.readMitarbeiter(empl);
    }

    @Override
    public MitarbeiterType get(GoogleUser user) {
        ReadMitarbeiterRequestType empl = new ReadMitarbeiterRequestType();
        empl.setRequestHeader(requestHeaderType);

        ReadMitarbeiterResponseType rmrt = zepSoapPortType.readMitarbeiter(empl);
        return rmrt.getMitarbeiterListe().getMitarbeiter().stream()
                .filter(emp -> user.getEmail().equals(emp.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Response updateWorker(List<MitarbeiterType> employees) {
        UpdateMitarbeiterRequestType umrt = new UpdateMitarbeiterRequestType();
        umrt.setRequestHeader(requestHeaderType);

        UpdateMitarbeiterResponseType umrest = null;
        for(MitarbeiterType mt : employees){
            umrt.setMitarbeiter(mt);
            umrest = zepSoapPortType.updateMitarbeiter(umrt);
            // TODO handle errors
        }
        return Response.ok(umrest).build();

    }
}
