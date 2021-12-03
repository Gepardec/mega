package com.gepardec.mega.application.exception.mapper;

import com.gepardec.mega.application.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnauthorizedExceptionMapperTest {

    @Mock
    private Logger logger;

    @Mock
    private UriInfo uriInfo;

    @InjectMocks
    private UnauthorizedExceptionMapper mapper;

    @Test
    void toResponse_whenCalled_thenReturnsUnauthorizedResponseAndLoggerWarnCalled() {
        when(uriInfo.getPath()).thenReturn("/path/resource");
        final Response response = mapper.toResponse(new UnauthorizedException("exception-message"));

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        verify(logger, times(1)).warn(anyString(), eq("/path/resource"), eq("exception-message"));
    }
}
