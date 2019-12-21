package com.gepardec.mega.zep.soap;

import com.sun.xml.ws.developer.JAXWSProperties;
import de.provantis.zep.RequestHeaderType;
import de.provantis.zep.ZepSoap;
import de.provantis.zep.ZepSoapPortType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.ws.BindingProvider;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class ZepSoapProvider {
    @Inject
    @ConfigProperty(name = "mega.zep.admin.token")
    String authorizationToken;


    @Inject
    @ConfigProperty(name = "mega.zep.admin.endpoint")
    String zepUrl;

    @Produces
    @Dependent
    ZepSoapPortType createZepSoapPortType() throws Exception {
        final ZepSoap zs = new ZepSoap(Thread.currentThread()
                .getContextClassLoader()
                .getResource("wsdl/Zep_V7.wsdl"));
        final ZepSoapPortType port = zs.getZepSOAP();
        configureWebserviceClient((BindingProvider) port, zepUrl, 5l, 10L, TimeUnit.SECONDS);
        return port;
    }

    public RequestHeaderType createRequestHeaderType() {
        RequestHeaderType requestHeaderType = new RequestHeaderType();
        requestHeaderType.setAuthorizationToken(authorizationToken);
        return requestHeaderType;
    }

    private void configureWebserviceClient(final BindingProvider bindingProvider, final String endpoint, final Long connectionTimeout, final Long requestTimeout, final TimeUnit timeoutUnit) {
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
        final int connectionTimeoutMillis = (int) TimeUnit.MILLISECONDS.convert(connectionTimeout, timeoutUnit);
        final int requestTimeoutMillis = (int) TimeUnit.MILLISECONDS.convert(requestTimeout, timeoutUnit);
        bindingProvider.getRequestContext().put(JAXWSProperties.CONNECT_TIMEOUT, connectionTimeoutMillis);
        bindingProvider.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, requestTimeoutMillis);
    }
}