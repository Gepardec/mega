package connector.service.impl;

import connector.rest.model.GoogleUser;
import connector.security.AuthorizationInterceptor;
import connector.service.api.WorkerService;
import de.provantis.zep.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Interceptors(AuthorizationInterceptor.class)
@ApplicationScoped
public class WorkerServiceImpl implements WorkerService {

    @Inject
    ZepSoapPortType zepSoapPortType;

    @Inject
    RequestHeaderType requestHeaderType;

    @Override
    public ReadMitarbeiterResponseType getAll(GoogleUser user) {
        ReadMitarbeiterRequestType empl = new ReadMitarbeiterRequestType();
        empl.setRequestHeader(requestHeaderType);

        ReadMitarbeiterResponseType rmrt = zepSoapPortType.readMitarbeiter(empl);
        // filter active employees
        // not active dont need to release times
        rmrt = filterActiveEmployees(rmrt);
        return rmrt;
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

    private ReadMitarbeiterResponseType filterActiveEmployees(ReadMitarbeiterResponseType rmrt) {
        List<MitarbeiterType> activeEmployees = new ArrayList<>();
        for (MitarbeiterType empl : rmrt.getMitarbeiterListe().getMitarbeiter()) {
            // only last Beschaeftigungszeit needed, its the latest
            int lastBeschaeftigungszeitIndex = empl.getBeschaeftigungszeitListe().getBeschaeftigungszeit().size() - 1;
            BeschaeftigungszeitType beschaeftigungszeitType = empl.getBeschaeftigungszeitListe()
                    .getBeschaeftigungszeit().get(lastBeschaeftigungszeitIndex);
            try {
                if (beschaeftigungszeitType.getEnddatum() == null ||
                        new SimpleDateFormat("yyyy-mm-dd")
                                .parse(beschaeftigungszeitType.getEnddatum()).compareTo(new Date()) >= 0) {
                    activeEmployees.add(empl);
                }
            } catch (ParseException e) {
                // TODO implement error handler
                e.printStackTrace();
            }
        }

        MitarbeiterListeType mlt = new MitarbeiterListeType();
        mlt.getMitarbeiter().addAll(activeEmployees);
        mlt.setLength(activeEmployees.size());

        rmrt.setMitarbeiterListe(mlt);
        return rmrt;
    }
}
