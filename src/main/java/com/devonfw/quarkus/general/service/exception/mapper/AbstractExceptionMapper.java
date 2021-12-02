package com.devonfw.quarkus.general.service.exception.mapper;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractExceptionMapper {

  private boolean exposeInternalErrorDetails = false;

  @Context
  UriInfo uriInfo;

  protected void logError(Exception exception) {

    log.error("Exception:{},URL:{},ERROR:{}", exception.getClass().getCanonicalName(), this.uriInfo.getRequestUri(),
        exception.getMessage());
  }

  protected Response createResponse(int status, String errorCode, Exception exception) {

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
