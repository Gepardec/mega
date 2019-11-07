package com.gepardec.mega.zep.soap;

import com.gepardec.mega.provider.TokenFileReadException;
import de.provantis.zep.RequestHeaderType;
import de.provantis.zep.ZepSoap;
import de.provantis.zep.ZepSoapPortType;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@ApplicationScoped
public class ZepSoapProvider {

    @Inject
    private Logger logger;

    private static final String ADMIN_TOKEN_FILE_NAME = "secret.soaptoken";

    private String authorizationToken;

    @PostConstruct
    private void initAuthorizationToken() {
        try {
            authorizationToken = Files.readString(Paths.get(ADMIN_TOKEN_FILE_NAME), StandardCharsets.UTF_8);
        } catch (IOException e) {
            String msg = "Error occured while reading file: " + ADMIN_TOKEN_FILE_NAME;
            logger.error(msg);
            throw new TokenFileReadException(msg);
        } catch (NullPointerException npe) {
            String msg = "File " + ADMIN_TOKEN_FILE_NAME + " not found, please put it in mega-zep-backend-resources";
            logger.error(msg);
            throw new TokenFileReadException(msg);
        }
    }


    @Produces
    @Dependent
    ZepSoapPortType createZepSoapPortType () {
        ZepSoap zs = new ZepSoap();
        return zs.getZepSOAP();
    }

    @Produces
    @Dependent
    RequestHeaderType createRequestHeaderType () {
        RequestHeaderType requestHeaderType = new RequestHeaderType();
        requestHeaderType.setAuthorizationToken(authorizationToken);
        return requestHeaderType;
    }
}
