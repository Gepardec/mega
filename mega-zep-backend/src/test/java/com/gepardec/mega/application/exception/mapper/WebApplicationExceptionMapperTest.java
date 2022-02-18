package com.gepardec.mega.application.exception.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(actual).isEqualTo(expected);
    }
}
