package com.devonfw.quarkus.general.service.exception.mapper;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<NotFoundException> {

  @Override
  public Response toResponse(NotFoundException exception) {

    logError(exception);

    return createResponse(Status.NOT_FOUND.getStatusCode(), exception.getClass().getSimpleName(), exception);
  }
}
