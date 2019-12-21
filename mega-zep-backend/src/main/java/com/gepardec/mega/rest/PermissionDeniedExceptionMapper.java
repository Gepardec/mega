package com.gepardec.mega.rest;

import com.gepardec.mega.security.PermissionDeniedException;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@ApplicationScoped
@Provider
public class PermissionDeniedExceptionMapper implements ExceptionMapper<PermissionDeniedException> {

    @Inject
    Logger logger;

    @Override
    public Response toResponse(PermissionDeniedException exception) {
        logger.warn("Permission denied detected. {}", exception.getMessage());
        return Response.accepted().status(Response.Status.FORBIDDEN).entity(exception.getMessage()).build();
    }
}
