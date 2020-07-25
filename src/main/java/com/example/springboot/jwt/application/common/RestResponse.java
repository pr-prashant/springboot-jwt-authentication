/**
 *
 */
package com.example.springboot.jwt.application.common;

import com.example.springboot.jwt.application.common.interfaces.IRestResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Prashant Patel
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class RestResponse<T> implements IRestResponse {

	/**
	 * Used to show status
	 */
	private boolean success;

	/**
	 * To map response errors
	 */
	private List<ErrorResponse> error = new ArrayList<>();

	/**
	 * Response data
	 */
	private T data;

	public RestResponse() {
	}

	/**
	 * Constructor to add error
	 * @param errorCode
	 * @param errorMessage
	 */
	public RestResponse(String errorCode, String errorMessage) {
		this.success = false;
		this.error.add(new ErrorResponse(errorCode, errorMessage));
	}

	/**
	 * Constructor to add data
	 * @param data
	 */
	public RestResponse(T data) {
		this.success = true;
		this.data = data;
	}

	/**
	 * @return the success
	 * @see RestResponse
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the error
	 * @see RestResponse
	 */
	public List<ErrorResponse> getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(List<ErrorResponse> error) {
		this.error = error;
	}

	/**
	 * @return the data
	 * @see RestResponse
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * Adding error to reponse
	 * @param errorCode
	 * @param errorMessage
	 */
	public void setErrorResponse(String errorCode, String errorMessage) {
		this.error.add(new ErrorResponse(errorCode, errorMessage));
	}

	/**
	 * Adding error to reponse
	 * @param errorCode
	 * @param errorMessage
	 * @param data
	 */
	public void setErrorResponse(String errorCode, String errorMessage, Object data) {
		this.error.add(new ErrorResponse(errorCode, errorMessage, data));
	}
}
