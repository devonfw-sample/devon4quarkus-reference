package com.devonfw.quarkus.general.rest.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import io.quarkus.security.UnauthorizedException;

@Provider
public class UnauthorizedExceptionMapper extends AbstractExceptionMapper
    implements ExceptionMapper<UnauthorizedException> {

  @Override
  public Response toResponse(UnauthorizedException exception) {

    logError(exception);

    return createResponse(Status.UNAUTHORIZED.getStatusCode(), exception.getClass().getSimpleName(), exception);
  }
}
