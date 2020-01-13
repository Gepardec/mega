package com.gepardec.mega.rest;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConstraintViolationResponse extends BaseResponse {

    private List<String> violations;

    public ConstraintViolationResponse() {
    }

    ConstraintViolationResponse(String message, Set<? extends ConstraintViolation<?>> violations) {
        super(message, false);
        this.violations = violations.stream()
                .map(javax.validation.ConstraintViolation::getMessage)
                .collect(Collectors.toList());
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
}
