package com.gepardec.mega.application.exception.mapper;

import com.gepardec.mega.domain.model.ValidationViolation;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Inject
    Logger logger;

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        logger.info("Constraint violation(s) on resource: '{}'", uriInfo.getPath());
        return Response.status(HttpStatus.SC_BAD_REQUEST)
                .entity(createResponseEntityForConstraintViolationException(exception))
                .build();
    }

    private List<ValidationViolation> createResponseEntityForConstraintViolationException(final ConstraintViolationException exception) {
        final Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        if (violations == null || violations.isEmpty()) {
            return List.of();
        }
        return violations.stream().map(this::createValidationViolationForConstraintViolation).collect(Collectors.toList());
    }

    private ValidationViolation createValidationViolationForConstraintViolation(final ConstraintViolation<?> violation) {
        return ValidationViolation.of(violation.getPropertyPath().toString(), violation.getMessage());
    }
}
