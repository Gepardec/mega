package com.gepardec.mega.application.exception.mapper;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

    @Inject
    Logger log;

    @Override
    public Response toResponse(Exception exception) {
        log.error("An unhandled exception occurred", exception);
        return Response.serverError().build();
    }
}
