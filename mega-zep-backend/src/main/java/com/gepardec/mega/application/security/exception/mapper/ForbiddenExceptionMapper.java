package com.gepardec.mega.application.security.exception.mapper;

import com.gepardec.mega.application.security.exception.ForbiddenException;
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
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    @Inject
    Logger logger;

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ForbiddenException exception) {
        logger.warn("Forbidden access on resource: '{}' with message: '{}'", uriInfo.getPath(), exception.getMessage());
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
