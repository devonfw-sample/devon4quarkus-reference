package com.devonfw.quarkus.exceptionutils;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

public class ProductNotFoundException extends NlsRuntimeException {

  private final String message;

  /**
   * The constructor.
   */
  public ProductNotFoundException(String message) {

    this.message = message;
  }

}
