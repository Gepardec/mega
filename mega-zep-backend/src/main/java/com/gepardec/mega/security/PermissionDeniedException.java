package com.gepardec.mega.security;

public class PermissionDeniedException extends SecurityException {
    public PermissionDeniedException() {
    }

    public PermissionDeniedException(String s) {
        super(s);
    }

    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionDeniedException(Throwable cause) {
        super(cause);
    }
}
