package com.gepardec.mega.rest.exception;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;

public class DefaultConstraintViolation implements ConstraintViolation<String> {

    private final String message;

    public DefaultConstraintViolation(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMessageTemplate() {
        return null;
    }

    @Override
    public String getRootBean() {
        return null;
    }

    @Override
    public Class<String> getRootBeanClass() {
        return null;
    }

    @Override
    public Object getLeafBean() {
        return null;
    }

    @Override
    public Object[] getExecutableParameters() {
        return new Object[0];
    }

    @Override
    public Object getExecutableReturnValue() {
        return null;
    }

    @Override
    public Path getPropertyPath() {
        return null;
    }

    @Override
    public Object getInvalidValue() {
        return null;
    }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() {
        return null;
    }

    @Override
    public <U> U unwrap(Class<U> type) {
        return null;
    }
}
