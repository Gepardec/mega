package connector.service.impl;

import connector.rest.model.GoogleUser;
import connector.service.api.WorkerService;
import de.provantis.zep.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class WorkerServiceImpl implements WorkerService {


    @Override
    public ReadMitarbeiterResponseType login(GoogleUser user) {
        ReadMitarbeiterRequestType ma = new ReadMitarbeiterRequestType();
        RequestHeaderType rht = new RequestHeaderType();
        rht.setAuthorizationToken("d51e04437e60eff16d0ee752e9b8f232ce82e943fb2759a79a3df92518778400");
        ma.setRequestHeader(rht);


        ZepSoap zs = new ZepSoap();
        ZepSoapPortType portType = zs.getZepSOAP();
        return portType.readMitarbeiter(ma);

    }
}
