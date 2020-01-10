package com.gepardec.mega.rest;

import com.gepardec.mega.security.ForbiddenException;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@ApplicationScoped
@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    @Inject
    Logger logger;

    @Override
    public Response toResponse(ForbiddenException exception) {
        logger.warn("Forbidden resource access. {}", exception.getMessage());
        return Response.accepted().status(Response.Status.FORBIDDEN).entity(exception.getMessage()).build();
    }
}
