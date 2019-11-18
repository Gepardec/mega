package com.gepardec.mega.zep.soap;

import com.gepardec.mega.provider.TokenFileReadException;
import de.provantis.zep.RequestHeaderType;
import de.provantis.zep.ZepSoap;
import de.provantis.zep.ZepSoapPortType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            final ZepSoap zs = new ZepSoap();
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
