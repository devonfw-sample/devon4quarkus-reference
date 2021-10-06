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
    }
    if (exception instanceof WebApplicationException) {
      return createResponse((WebApplicationException) exception);
    }
    return createResponse(exception);
  }

  private Response createResponse(ApplicationBusinessException exception) {

    return createResponse(Status.BAD_REQUEST, Error.APPLICATION_BUSINESS_EXCEPTION, exception);
  }

  private Response createResponse(WebApplicationException exception) {

    Status status = Status.fromStatusCode(exception.getResponse().getStatus());
    return createResponse(status, Error.WEB_APPLICATION_EXCEPTION, exception);
  }

  private Response createResponse(Exception exception) {

    return createResponse(Status.INTERNAL_SERVER_ERROR, Error.UNDEFINED_ERROR_CODE, exception);
  }

  private Response createResponse(Status status, Error errorCode, Exception exception) {

    Map<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("errorCode", errorCode.name());
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

  public enum Error {

    APPLICATION_BUSINESS_EXCEPTION, WEB_APPLICATION_EXCEPTION, UNDEFINED_ERROR_CODE;
  }

}
