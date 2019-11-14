/*
 * Copyright (c) 2019 by gepardec GmbH.
 * Autor: Philipp Wurm <philipp.wurm@gepardec.com>
 * Create date: 02.05.19 19:16
 */

package com.gepardec.mega.filter;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSResponseFilter implements ContainerResponseFilter {

//    @ConfigProperty(name = "quarkus.http.cors")
//    String accessControlAllowCredentials;
//
//    @ConfigProperty(name = "quarkus.http.cors.origins")
//    String accessControlAllowOrigin;
//
//    @ConfigProperty(name = "quarkus.http.cors.methods")
//    String accessControlAllowMethods;
//
//    @ConfigProperty(name = "quarkus.http.cors.headers")
//    String accessControlAllowHeaders;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
//        responseContext.getHeaders().add("Access-Control-Allow-Credentials", accessControlAllowCredentials);
//        responseContext.getHeaders().add("Access-Control-Allow-Origin", accessControlAllowOrigin);
//        responseContext.getHeaders().add("Access-Control-Allow-Methods", accessControlAllowMethods);
//        responseContext.getHeaders().add("Access-Control-Allow-Headers", accessControlAllowHeaders);

        // in case of options for cors-post - return status 200
        if(requestContext.getMethod().equals(HttpMethod.OPTIONS)) {
            responseContext.setStatus(HttpStatus.SC_OK);
        }
    }
}
