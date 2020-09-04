package com.gepardec.mega.application.exception.mapper;

import com.gepardec.mega.application.exception.ForbiddenException;
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
public class ForbiddenExceptionMapperTest {

    @Mock
    private Logger logger;

    @Mock
    private UriInfo uriInfo;

    @InjectMocks
    private ForbiddenExceptionMapper mapper;

    @Test
    void toResponse_whenCalled_thenReturnsForbiddenResponseAndLoggerWarnCalled() {
        when(uriInfo.getPath()).thenReturn("/path/resource");
        final Response response = mapper.toResponse(new ForbiddenException("exception-message"));

        Assertions.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
        verify(logger, times(1)).warn(anyString(), eq("/path/resource"), eq("exception-message"));
    }
}
