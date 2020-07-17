package com.gepardec.mega.rest.model;

// FIXME GAJ:
public class BaseResponse {

    private String message;
    private boolean success = true;
    private int error = 0;

    BaseResponse() {
    }

    BaseResponse(String message, int error) {
        this.message = message;
        this.success = error != 0;
        this.error = error;
    }

    public static BaseResponse success(final String message) {
        return new BaseResponse(message, 0);
    }

    public static BaseResponse error(final String message, int error) {
        return new BaseResponse(message, error);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
