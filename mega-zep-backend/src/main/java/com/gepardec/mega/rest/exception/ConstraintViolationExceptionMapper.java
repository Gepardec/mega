package com.gepardec.mega.rest.exception;

import com.gepardec.mega.rest.ConstraintViolationResponse;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@RequestScoped
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Inject
    Logger logger;

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        logger.info("Constraint violation(s) on resource: '{}'", uriInfo.getPath());
        return Response.status(HttpStatus.SC_BAD_REQUEST)
                .entity(ConstraintViolationResponse.invalid("Der Aufruf ist ungültig wegen Validierungsfehlern",
                        exception.getConstraintViolations()))
                .build();
    }
}
