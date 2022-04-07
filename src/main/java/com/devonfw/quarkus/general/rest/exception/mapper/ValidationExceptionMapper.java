package com.devonfw.quarkus.general.rest.exception.mapper;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<ValidationException> {

  @Override
  public Response toResponse(ValidationException exception) {

    logError(exception);

    return createResponse(Status.BAD_REQUEST.getStatusCode(), exception.getClass().getSimpleName(), exception);
  }
}
