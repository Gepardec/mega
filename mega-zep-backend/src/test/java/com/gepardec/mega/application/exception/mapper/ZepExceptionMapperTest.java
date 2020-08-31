package com.gepardec.mega.application.exception.mapper;

import com.gepardec.mega.zep.ZepServiceException;
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
public class ZepExceptionMapperTest {

    @Mock
    private Logger logger;

    @Mock
    private UriInfo uriInfo;

    @InjectMocks
    private ZepExceptionMapper mapper;

    @Test
    void toResponse_whenCalled_thenReturnsInternalErrorResponseAndLoggerErrorCalled() {
        when(uriInfo.getPath()).thenReturn("/path/resource");
        final Response response = mapper.toResponse(new ZepServiceException("exception-message"));

        Assertions.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        verify(logger, times(1)).error(anyString(), eq("/path/resource"), eq("exception-message"));
    }
}
