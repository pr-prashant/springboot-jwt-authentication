package com.example.springboot.jwt.application.common.exceptions;

public class GenericServiceException extends Exception {

    private int errorCode;

    public GenericServiceException() {
        super();
    }

    public GenericServiceException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GenericServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericServiceException(String message) {
        super(message);
    }

    public GenericServiceException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public GenericServiceException(Throwable cause) {
        super(cause);
    }

    public int getErrorCode() {
        return errorCode;
    }
}
