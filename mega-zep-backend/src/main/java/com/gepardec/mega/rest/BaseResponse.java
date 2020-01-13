package com.gepardec.mega.rest;

public class BaseResponse {

    private String message;
    private boolean success = true;

    BaseResponse() {
    }

    BaseResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public static BaseResponse success(final String message) {
        return new BaseResponse(message, true);
    }

    public static BaseResponse error(final String message) {
        return new BaseResponse(message, false);
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
}
