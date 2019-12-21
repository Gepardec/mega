package com.gepardec.mega.rest;

import com.gepardec.mega.security.UnauthorizedException;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@ApplicationScoped
@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

    @Inject
    Logger logger;

    @Override
    public Response toResponse(UnauthorizedException exception) {
        logger.warn("Unauthorized access detected. {}", exception.getMessage());
        return Response.accepted().status(Response.Status.UNAUTHORIZED).build();
    }
}
