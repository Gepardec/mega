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

    private static final String ZEP_SOAP_TOKEN_FILE_NAME = "secret.soaptoken";
    private String authorizationToken;

    @PostConstruct
    void initAuthorizationToken() {
        Stream<String> lines = null;
        try {
            final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(ZEP_SOAP_TOKEN_FILE_NAME)).toURI());
            lines = Files.lines(path);
            authorizationToken = lines.collect(Collectors.joining("\n"));
        } catch (IOException e) {
            String msg = "Error occured while reading file: " + ZEP_SOAP_TOKEN_FILE_NAME;
            logger.error(msg);
            throw new TokenFileReadException(msg);
        } catch (NullPointerException npe) {
            String msg = "File " + ZEP_SOAP_TOKEN_FILE_NAME + " not found, please put it in mega-zep-backend-resources";
            logger.error(msg);
            throw new TokenFileReadException(msg);
        } catch (URISyntaxException e) {
            logger.error("error read authorizationToken {}", e.getMessage());
        } finally {
            if (lines != null) {
                lines.close();
            }
        }
    }


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
