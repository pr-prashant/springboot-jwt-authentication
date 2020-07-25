/**
 *
 */
package com.example.springboot.jwt.application.common;

/**
 * Class to map all errors
 *
 * @author Prashant Patel
 */
public class ErrorResponse {

    private String code;
    private String message;
    private Object data;

    /**
     * @param code
     * @param message
     */
    public ErrorResponse() {
        this.code = "";
        this.message = "";
        this.data = null;
    }

    /**
     * @param code
     * @param message
     */
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    /**
     * @param code
     * @param message
     * @param data
     */
    public ErrorResponse(String code, String message, Object data) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    /**
     * @return the code
     * @see ErrorResponse
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the message
     * @see ErrorResponse
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return error data
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data
     * 			error data to be set
     */
    public void setData(Object data) {
        this.data = data;
    }
}
