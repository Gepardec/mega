package com.gepardec.mega.application.exception;

public class UnauthorizedException extends SecurityException {

    public UnauthorizedException() {
    }

    public UnauthorizedException(String s) {
        super(s);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause);
    }
}
