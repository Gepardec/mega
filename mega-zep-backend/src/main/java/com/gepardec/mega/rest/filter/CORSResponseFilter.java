/*
 * Copyright (c) 2019 by gepardec GmbH.
 * Autor: Philipp Wurm <philipp.wurm@gepardec.com>
 * Create date: 02.05.19 19:16
 */

package com.gepardec.mega.rest.filter;

import org.apache.http.HttpStatus;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

// TODO: Should be obsolete. Configure quarkus cors properly
@Provider
public class CORSResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {

        // in case of options for cors-post - return status 200
        if (requestContext.getMethod().equals(HttpMethod.OPTIONS)) {
            responseContext.setStatus(HttpStatus.SC_OK);
        }
    }
}
