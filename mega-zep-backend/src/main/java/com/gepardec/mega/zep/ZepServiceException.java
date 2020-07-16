package com.gepardec.mega.zep;

public class ZepServiceException extends RuntimeException {

    public ZepServiceException() {
    }

    public ZepServiceException(String message) {
        super(message);
    }

    public ZepServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
