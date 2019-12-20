package com.gepardec.mega.rest;

import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@ApplicationScoped
@Provider
public class SecurityExceptionMapper implements ExceptionMapper<SecurityException> {

    @Inject
    Logger logger;

    @Override
    public Response toResponse(SecurityException exception) {
        logger.error("Security exception for ", exception);
        return Response.accepted().status(Response.Status.UNAUTHORIZED).entity(exception.getMessage()).build();
    }
}
