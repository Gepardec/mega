package com.gepardec.mega.application.exception.mapper;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@QuarkusTest
class ApplicationExceptionMapperTest {

    Logger logger;

    @Inject
    @SuppressWarnings("CdiInjectionPointsInspection")
    ApplicationExceptionMapper mapper;

    @BeforeEach
    void setup() {
        logger = spy(Logger.class);
        mapper.log = logger;
    }

    @Test
    void toResponse_whenCalled_thenReturnsInternalErrorResponseReturnedAndLoggerErrorCalled() {
        final Response response = mapper.toResponse(new IllegalArgumentException());

        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        verify(logger, times(1)).error(anyString(), any(IllegalArgumentException.class));
    }
}
