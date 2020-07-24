package com.gepardec.mega.rest.exception;

import com.gepardec.mega.application.exception.mapper.ConstraintViolationExceptionMapper;
import com.gepardec.mega.rest.model.ConstraintViolationResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConstraintViolationExceptionMapperTest {

    @Mock
    private Logger log;

    @Mock
    private UriInfo uriInfo;

    @InjectMocks
    private ConstraintViolationExceptionMapper exceptionMapper;

    @Test
    void toResponse_whenException_thenProperResponseBuilt() {
        when(uriInfo.getPath()).thenReturn("/rest/path");
        final Set<? extends ConstraintViolation<?>> violations = new HashSet<>() {{
            add(new DefaultConstraintViolation("Property 1"));
            add(new DefaultConstraintViolation("Property 2"));
        }};
        final List<String> expectedViolations = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        final ConstraintViolationException exception = new ConstraintViolationException(violations);
        final Response response = exceptionMapper.toResponse(exception);

        final ConstraintViolationResponse responseModel = (ConstraintViolationResponse) response.getEntity();
        verify(log, times(1)).info(anyString(), any(Object.class));
        assertAll(
                () -> assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus(), "status"),
                () -> assertIterableEquals(expectedViolations, responseModel.getViolations(), "violations"));
    }
}
