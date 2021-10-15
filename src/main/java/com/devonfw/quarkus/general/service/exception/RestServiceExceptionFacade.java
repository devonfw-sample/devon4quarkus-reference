package com.devonfw.quarkus.general.service.exception;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class RestServiceExceptionFacade implements ExceptionMapper<RuntimeException> {

  private boolean exposeInternalErrorDetails = false;

  @Context
  UriInfo uriInfo;

  @Override
  public Response toResponse(RuntimeException exception) {

    log.error("Exception:{},URL:{},ERROR:{}", exception.getClass().getCanonicalName(), this.uriInfo.getRequestUri(),
        exception.getMessage());

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

  private Response createResponse(int status, String errorCode, Exception exception) {

    Map<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("errorCode", errorCode);
    if (this.exposeInternalErrorDetails) {
      jsonMap.put("message", getExposedErrorDetails(exception));
    } else {
      jsonMap.put("message", exception.getMessage());
    }
    jsonMap.put("uri", this.uriInfo.getPath());
    jsonMap.put("timestamp", ZonedDateTime.now().toString());
    return Response.status(status).type(MediaType.APPLICATION_JSON).entity(jsonMap).build();
  }

  private String getExposedErrorDetails(Throwable error) {

    StringBuilder buffer = new StringBuilder();
    Throwable e = error;
    while (e != null) {
      if (buffer.length() > 0) {
        buffer.append(System.lineSeparator());
      }
      buffer.append(e.getClass().getSimpleName());
      buffer.append(": ");
      buffer.append(e.getLocalizedMessage());
      e = e.getCause();
    }
    return buffer.toString();
  }

}
