package com.gepardec.mega.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.xml.ws.WebServiceException;

public class WebserviceExceptionMapper implements ExceptionMapper<WebServiceException> {
    @Override
    public Response toResponse(WebServiceException exception) {
        return Response.serverError().entity(exception.getMessage()).build();
    }
}
