package com.gepardec.mega.application.security.exception;

public class ForbiddenException extends SecurityException {
    public ForbiddenException() {
    }

    public ForbiddenException(String s) {
        super(s);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenException(Throwable cause) {
        super(cause);
    }
}
