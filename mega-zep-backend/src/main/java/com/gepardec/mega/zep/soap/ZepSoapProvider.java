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

    @ConfigProperty(name = "mega.zep.admin.token")
    String authorizationToken;


    @Produces
    @Dependent
    @Named("ZepAuthorizationSOAPPortType")
    public ZepSoapPortType createZepSoapPortType() {
        try {
            final ZepSoap zs = new ZepSoap();
            return zs.getZepSOAP();
        } catch (Exception e) {
            logger.error("Error createZepSoapPortType {}", e.getMessage());
            return null;
        }
    }

    @Produces
    @Dependent
    @Named("ZepAuthorizationToken")
    public String zepAuthorizationToken() {
        return authorizationToken;
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