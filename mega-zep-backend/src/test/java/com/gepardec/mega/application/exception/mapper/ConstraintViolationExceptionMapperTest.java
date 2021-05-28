package com.gepardec.mega.application.exception.mapper;

import com.gepardec.mega.domain.model.ValidationViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ConstraintViolationExceptionMapperTest {

    @Mock
    private Logger logger;

    @Mock
    private UriInfo uriInfo;

    @Mock
    private Validator validator;

    @InjectMocks
    private ConstraintViolationExceptionMapper mapper;

    @BeforeEach
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void toResponse_whenNullViolations_thenReturnsEmptyList() {
        final Response response = mapper.toResponse(new ConstraintViolationException(null));
        List<ValidationViolation> actual = (List<ValidationViolation>) response.getEntity();

        assertTrue(actual.isEmpty());
    }

    @Test
    void toResponse_whenEmptyViolations_thenReturnsEmptyList() {
        final Response response = mapper.toResponse(new ConstraintViolationException(Set.of()));
        List<ValidationViolation> actual = (List<ValidationViolation>) response.getEntity();

        assertTrue(actual.isEmpty());
    }

    @Test
    void toResponse_whenViolations_thenReturnsMappedValidationViolationList() {
        final Set<ConstraintViolation<Model>> violations = validator.validate(new Model());
        final Response response = mapper.toResponse(new ConstraintViolationException(violations));
        List<ValidationViolation> actual = (List<ValidationViolation>) response.getEntity();

        assertEquals(2, actual.size());
        assertTrue(actual.containsAll(List.of(
                ValidationViolation.of("name", "must not be null"),
                ValidationViolation.of("email", "must not be null"))));
    }
    public static class Model {

        @NotNull(message = "must not be null")
        private String name;

        @NotNull(message = "must not be null")
        private String email;
    }
}
