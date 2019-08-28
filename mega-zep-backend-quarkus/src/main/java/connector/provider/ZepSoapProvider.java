package connector.provider;

import de.provantis.zep.RequestHeaderType;
import de.provantis.zep.ZepSoap;
import de.provantis.zep.ZepSoapPortType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class ZepSoapProvider {

    private final String AUTHORIZATION_TOKEN = "d51e04437e60eff16d0ee752e9b8f232ce82e943fb2759a79a3df92518778400";

    @Produces
    @Dependent
    ZepSoapPortType createZepSoapPortType() {
        ZepSoap zs = new ZepSoap();
        return zs.getZepSOAP();
    }

    @Produces
    @Dependent
    RequestHeaderType createRequestHeaderType() {
        RequestHeaderType requestHeaderType = new RequestHeaderType();
        requestHeaderType.setAuthorizationToken(AUTHORIZATION_TOKEN);
        return requestHeaderType;
    }


}
