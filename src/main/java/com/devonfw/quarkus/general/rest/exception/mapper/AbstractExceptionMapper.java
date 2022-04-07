package com.devonfw.quarkus.general.rest.exception.mapper;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * Abstract super class for all specific exception mapper. To override the default ExceptionMapper of RESTEasy, own
 * ExceptionMapper for specific exceptions (e.g. NotFoundException) have to be created. Just using
 * ExcecptionMapper<Throwable> will not work, because the RESTEasy mappers are then more specific.
 *
 *
 * @see <a href="https://github.com/quarkusio/quarkus/issues/7883">Quarkus Issue 7883</a>
 *
 */
@Slf4j
public abstract class AbstractExceptionMapper {

  protected boolean exposeInternalErrorDetails = false;

  @Context
  UriInfo uriInfo;

  protected void logError(Exception exception) {

    log.error("Exception:{},URL:{},ERROR:{}", exception.getClass().getCanonicalName(), this.uriInfo.getRequestUri(),
        exception.getMessage());
  }

  protected Response createResponse(int status, String errorCode, Exception exception) {

    Map<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("code", errorCode);
    if (this.exposeInternalErrorDetails) {
      jsonMap.put("message", getExposedErrorDetails(exception));
    } else {
      jsonMap.put("message", exception.getMessage());
    }
    jsonMap.put("uri", this.uriInfo.getPath());
    jsonMap.put("uuid", UUID.randomUUID());
    jsonMap.put("timestamp", ZonedDateTime.now().toString());
    return Response.status(status).type(MediaType.APPLICATION_JSON).entity(jsonMap).build();
  }

  protected String getExposedErrorDetails(Throwable error) {

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
