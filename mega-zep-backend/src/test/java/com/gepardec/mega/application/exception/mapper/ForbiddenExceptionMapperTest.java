package com.gepardec.mega.application.exception.mapper;

import com.gepardec.mega.application.exception.ForbiddenException;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Set;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForbiddenExceptionMapperTest {

    @Mock
    private Logger logger;

    @Mock
    private UriInfo uriInfo;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private ForbiddenExceptionMapper mapper;

    @Test
    void toResponse_whenUserNotLogged_thenLoggerWarnCalledAndMessageContainsUnknown() {
        when(uriInfo.getPath()).thenReturn("/path/resource");
        final Response response = mapper.toResponse(new ForbiddenException("exception-message"));

        verify(logger, times(1)).warn(anyString(), eq("unknown"), eq("/path/resource"), eq("exception-message"));
    }

    @Test
    void toResponse_whenUserNotLogged_thenLoggerWarnCalledAndMessageContainsUserEmail() {
        when(userContext.user()).thenReturn(createUserForEmail("thomas.herzog@gepardec.com"));
        when(uriInfo.getPath()).thenReturn("/path/resource");
        final Response response = mapper.toResponse(new ForbiddenException("exception-message"));

        Assertions.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
        verify(logger, times(1)).warn(anyString(), eq("thomas.herzog@gepardec.com"), eq("/path/resource"), eq("exception-message"));
    }

    @Test
    void toResponse_whenCalled_thenReturnsHttpErrorFORBIDDEN() {
        when(userContext.user()).thenReturn(createUserForEmail("thomas.herzog@gepardec.com"));
        when(uriInfo.getPath()).thenReturn("/path/resource");
        final Response response = mapper.toResponse(new ForbiddenException("exception-message"));

        Assertions.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    private User createUserForEmail(final String email) {
        return User.builder()
                .dbId(1)
                .userId("1")
                .email(email)
                .firstname("Thomas")
                .lastname("Herzog")
                .roles(Set.of(Role.EMPLOYEE))
                .build();
    }
}
