package com.gepardec.mega.application.exception.mapper;

import com.gepardec.mega.zep.ZepServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZepExceptionMapperTest {

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

        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        verify(logger, times(1)).error(anyString(), eq("/path/resource"), eq("exception-message"));
    }
}
