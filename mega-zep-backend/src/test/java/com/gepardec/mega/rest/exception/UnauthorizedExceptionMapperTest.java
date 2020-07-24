package com.gepardec.mega.rest.exception;

import com.gepardec.mega.application.security.exception.UnauthorizedException;
import com.gepardec.mega.application.security.exception.mapper.UnauthorizedExceptionMapper;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnauthorizedExceptionMapperTest {

    @Mock
    private Logger log;

    @Mock
    private UriInfo uriInfo;

    @InjectMocks
    private UnauthorizedExceptionMapper exceptionMapper;

    @Test
    void toResponse_whenException_thenProperResponseBuilt() {
        when(uriInfo.getPath()).thenReturn("/rest/path");
        final UnauthorizedException exception = new UnauthorizedException("User not valid");
        final Response response = exceptionMapper.toResponse(exception);

        verify(log, times(1)).warn(anyString(), any(Object.class), any(Object.class));
        assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatus(), "status");
    }
}
