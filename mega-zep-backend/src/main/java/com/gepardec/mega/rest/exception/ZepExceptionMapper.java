package com.gepardec.mega.rest.exception;

import com.gepardec.mega.zep.exception.ZepServiceException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ZepExceptionMapper implements ExceptionMapper<ZepServiceException> {

    @Inject
    Logger log;

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ZepServiceException exception) {
        log.error("ZEP Service error on resource: '{}' with message: '{}'", uriInfo.getPath(), exception.getMessage());
        return Response.serverError().build();
    }
}
