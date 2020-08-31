package com.gepardec.mega.rest.model;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ConstraintViolationResponse extends BaseResponse {

    private List<String> violations;

    public ConstraintViolationResponse() {
        this(null, Collections.emptySet());
    }

    ConstraintViolationResponse(String message, Set<? extends ConstraintViolation<?>> violations) {
        super(message, 1);
        this.violations = (violations != null) ? violations.stream()
                .map(javax.validation.ConstraintViolation::getMessage)
                .collect(Collectors.toList())
                : Collections.emptyList();
    }

    public static ConstraintViolationResponse invalid(String message, Set<? extends ConstraintViolation<?>> violations) {
        return new ConstraintViolationResponse(message, violations);
    }

    public List<String> getViolations() {
        return violations;
    }

    public void setViolations(List<String> violations) {
        this.violations = violations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstraintViolationResponse that = (ConstraintViolationResponse) o;
        return violations.equals(that.violations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(violations);
    }
}
