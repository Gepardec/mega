package com.gepardec.mega.rest.exception;

import com.gepardec.mega.application.exception.ForbiddenException;
import com.gepardec.mega.application.exception.mapper.ForbiddenExceptionMapper;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForbiddenExceptionMapperTest {

    @Mock
    private Logger log;

    @Mock
    private UriInfo uriInfo;

    @InjectMocks
    private ForbiddenExceptionMapper exceptionMapper;

    @Test
    void toResponse_whenException_thenProperResponseBuilt() {
        when(uriInfo.getPath()).thenReturn("/rest/path");
        final ForbiddenException exception = new ForbiddenException("User has no role");
        final Response response = exceptionMapper.toResponse(exception);

        verify(log, times(1)).warn(anyString(), any(Object.class), any(Object.class));
        Assertions.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatus(), "status");
    }
}
