package com.devonfw.quarkus.general.rest.exception.mapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.devonfw.quarkus.general.rest.exception.ApplicationBusinessException;

@Provider
public class RuntimeExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<RuntimeException> {

  @Override
  public Response toResponse(RuntimeException exception) {

    logError(exception);

    if (exception instanceof ApplicationBusinessException) {
      return createResponse((ApplicationBusinessException) exception);
    } else if (exception instanceof WebApplicationException) {
      return createResponse((WebApplicationException) exception);
    }

    return createResponse(exception);
  }

  private Response createResponse(ApplicationBusinessException exception) {

    int status = exception.getStatusCode() == null ? Status.BAD_REQUEST.getStatusCode() : exception.getStatusCode();
    return createResponse(status, exception.getCode(), exception);
  }

  private Response createResponse(WebApplicationException exception) {

    Status status = Status.fromStatusCode(exception.getResponse().getStatus());
    return createResponse(status.getStatusCode(), exception.getClass().getSimpleName(), exception);
  }

  private Response createResponse(Exception exception) {

    return createResponse(Status.INTERNAL_SERVER_ERROR.getStatusCode(), exception.getClass().getSimpleName(),
        exception);
  }

}
