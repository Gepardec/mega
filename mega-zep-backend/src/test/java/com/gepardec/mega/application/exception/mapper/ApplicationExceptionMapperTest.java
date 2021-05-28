package com.gepardec.mega.application.exception.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ApplicationExceptionMapperTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private ApplicationExceptionMapper mapper;

    @Test
    void toResponse_whenCalled_thenReturnsInternalErrorResponseReturnedAndLoggerErrorCalled() {
        final Response response = mapper.toResponse(new IllegalArgumentException());

        Assertions.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        verify(logger, times(1)).error(anyString(), any(IllegalArgumentException.class));
    }
}
