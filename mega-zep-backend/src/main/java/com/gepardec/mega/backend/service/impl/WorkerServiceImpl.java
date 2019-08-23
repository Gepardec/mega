package com.gepardec.mega.backend.service.impl;

import com.gepardec.mega.backend.rest.model.GoogleUser;
import com.gepardec.mega.backend.service.api.WorkerService;
import de.provantis.zep.*;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.List;

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

    @Override
    public Response updateWorker(List<MitarbeiterType> employees) {
        UpdateMitarbeiterRequestType umrt = new UpdateMitarbeiterRequestType();
        RequestHeaderType rht = new RequestHeaderType();
        rht.setAuthorizationToken("d51e04437e60eff16d0ee752e9b8f232ce82e943fb2759a79a3df92518778400");
        umrt.setRequestHeader(rht);


        ZepSoap zs = new ZepSoap();
        ZepSoapPortType portType = zs.getZepSOAP();
        for(MitarbeiterType mt : employees){
            umrt.setMitarbeiter(mt);
            UpdateMitarbeiterResponseType umrest = portType.updateMitarbeiter(umrt);
        }
        return Response.ok().build();

    }
}
