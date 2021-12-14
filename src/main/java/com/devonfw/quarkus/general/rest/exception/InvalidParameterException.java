package com.devonfw.quarkus.general.rest.exception;

public class InvalidParameterException extends ApplicationBusinessException {

  public InvalidParameterException() {

    super();
  }

  public InvalidParameterException(String message) {

    super(message);
  }

  @Override
  public Integer getStatusCode() {

    return Integer.valueOf(422);
  }
}
