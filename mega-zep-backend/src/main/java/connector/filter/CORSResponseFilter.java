/*
 * Copyright (c) 2019 by gepardec GmbH.
 * Autor: Philipp Wurm <philipp.wurm@gepardec.com>
 * Create date: 02.05.19 19:16
 */

package connector.filter;

//import org.apache.http.HttpStatus;

import org.apache.http.HttpStatus;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CORSResponseFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:4200");
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

        // in case of options for cors-post - return status 200
        if(requestContext.getMethod().equals("OPTIONS")) {
            responseContext.setStatus(HttpStatus.SC_OK);
        }
    }
}
