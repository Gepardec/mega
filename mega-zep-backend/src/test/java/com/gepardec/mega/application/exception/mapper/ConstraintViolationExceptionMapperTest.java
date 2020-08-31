package com.gepardec.mega.application.exception.mapper;

import com.gepardec.mega.rest.model.ConstraintViolationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConstraintViolationExceptionMapperTest {

    @Mock
    private Logger logger;

    @Mock
    private UriInfo uriInfo;

    @Mock
    private ResourceBundle resourceBundle;

    @InjectMocks
    private ConstraintViolationExceptionMapper mapper;

    @Test
    void toResponse_whenCalled_thenReturnsBadRequestResponseAndLoggerInfoCalled() {
        when(uriInfo.getPath()).thenReturn("/path/resource");
        when(resourceBundle.getString("response.exception.bad-request")).thenReturn("ConstraintViolationException");
        final ConstraintViolationResponse violationResponse = ConstraintViolationResponse.invalid("ConstraintViolationException",
                Collections.emptySet());

        final Response response = mapper.toResponse(new ConstraintViolationException(Collections.emptySet()));

        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        Assertions.assertEquals(violationResponse, response.getEntity());
        verify(logger, times(1)).info(anyString(), eq("/path/resource"));
    }

}
