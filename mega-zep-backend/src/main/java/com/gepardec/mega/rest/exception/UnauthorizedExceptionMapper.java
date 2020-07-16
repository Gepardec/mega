package com.gepardec.mega.rest.exception;

import com.gepardec.mega.application.security.UnauthorizedException;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@ApplicationScoped
@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

    @Inject
    Logger logger;

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(UnauthorizedException exception) {
        logger.warn("Unauthorized access on resource: '{}' with message: '{}'", uriInfo.getPath(), exception.getMessage());
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
