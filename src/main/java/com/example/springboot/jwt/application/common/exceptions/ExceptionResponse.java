package com.example.springboot.jwt.application.common.exceptions;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "error")
public class ExceptionResponse {

  private int statusCode;
  private String errorMessage;

  /**
   * @return the statusCode
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * @param statusCode the statusCode to set
   */
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  /**
   * @return the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * @param errorMessage the errorMessage to set
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }


}
