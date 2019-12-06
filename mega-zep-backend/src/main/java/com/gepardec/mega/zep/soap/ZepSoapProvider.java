package com.gepardec.mega.zep.soap;

import de.provantis.zep.RequestHeaderType;
import de.provantis.zep.ZepSoap;
import de.provantis.zep.ZepSoapPortType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class ZepSoapProvider {

    @Inject
    Logger logger;

    @Inject
    @ConfigProperty(name = "zep.soap.token")
    String authorizationToken;

    @Produces
    @Dependent
    @Named("ZepAuthorizationSOAPPortType")
    public ZepSoapPortType createZepSoapPortType() {
        try {
            final ZepSoap zs = new ZepSoap(Thread.currentThread()
                    .getContextClassLoader()
                    .getResource("wsdl/Zep_V7.wsdl"));
            return zs.getZepSOAP();
        } catch (Exception e) {
            logger.error("Could not create zep soap port", e);
        }

        return null;
    }

    @Produces
    @Dependent
    @Named("ZepAuthorizationRequestHeaderType")
    public RequestHeaderType createRequestHeaderType() {
        RequestHeaderType requestHeaderType = new RequestHeaderType();
        requestHeaderType.setAuthorizationToken(authorizationToken);
        return requestHeaderType;
    }
}
