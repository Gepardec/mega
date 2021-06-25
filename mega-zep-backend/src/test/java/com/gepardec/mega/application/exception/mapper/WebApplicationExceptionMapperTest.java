package com.gepardec.mega.application.exception.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class WebApplicationExceptionMapperTest {

    private WebApplicationExceptionMapper mapper;

    @BeforeEach
    void beforeEach() {
        mapper = new WebApplicationExceptionMapper();
    }

    @Test
    void toResponse_whenCalled_thenReturnsWebApplicationExceptionContainedResponse() {
        final Response expected = Response.status(Response.Status.CONFLICT).build();
        final Response actual = mapper.toResponse(new WebApplicationException(expected));

        Assertions.assertEquals(expected, actual);
    }
}
