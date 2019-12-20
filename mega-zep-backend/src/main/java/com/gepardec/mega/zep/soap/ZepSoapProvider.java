package com.gepardec.mega.zep.soap;

import de.provantis.zep.RequestHeaderType;
import de.provantis.zep.ZepSoap;
import de.provantis.zep.ZepSoapPortType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
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
    public ZepSoapPortType createZepSoapPortType() throws Exception {
        final ZepSoap zs = new ZepSoap(Thread.currentThread()
                .getContextClassLoader()
                .getResource("wsdl/Zep_V7.wsdl"));
        final ZepSoapPortType port = zs.getZepSOAP();
        configureWebserviceClient((BindingProvider) port, zepUrl, 30, TimeUnit.SECONDS);
        return port;
    }

    @Produces
    @Dependent
    public RequestHeaderType createRequestHeaderType() {
        RequestHeaderType requestHeaderType = new RequestHeaderType();
        requestHeaderType.setAuthorizationToken(authorizationToken);
        return requestHeaderType;
    }

    private void configureWebserviceClient(final BindingProvider bindingProvider, final String endpoint, final Integer timeout, final TimeUnit timeoutUnit) {
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
        setTimeouts(bindingProvider, timeout, timeoutUnit);
    }

    private void setTimeouts(final BindingProvider bindingProvider, final Integer timeout, final TimeUnit timeoutUnit) {
        final long timeoutMilis = TimeUnit.MILLISECONDS.convert(timeout, timeoutUnit);
        //TODO: check com.sun.xml.ws - SOAP timeouts
        bindingProvider.getRequestContext().put("com.sun.xml.ws.request.timeout", timeoutMilis);
        bindingProvider.getRequestContext().put("javax.xml.ws.client.receiveTimeout", timeoutMilis);
    }
}