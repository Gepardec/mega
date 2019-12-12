package com.gepardec.mega.communication;

public class MissingLogoException extends RuntimeException {
    public MissingLogoException(String message) {
        super(message);
    }

    public MissingLogoException(String message, Throwable cause) {
        super(message, cause);
    }
}
