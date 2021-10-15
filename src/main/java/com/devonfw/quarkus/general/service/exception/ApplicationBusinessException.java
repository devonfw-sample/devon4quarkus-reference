package com.devonfw.quarkus.general.service.exception;

public abstract class ApplicationBusinessException extends RuntimeException {

  public ApplicationBusinessException() {

    super();
  }

  public ApplicationBusinessException(String message) {

    super(message);
  }

  public ApplicationBusinessException(String message, Exception e) {

    super(message, e);
  }

  public boolean isTechnical() {

    return false;
  }

  public String getCode() {

    return getClass().getSimpleName();
  }

  public Integer getStatusCode() {

    return null;
  }
}
