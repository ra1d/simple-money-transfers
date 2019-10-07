package com.shcheglov.task.api.mapping;

import io.dropwizard.jersey.errors.ErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author anton
 */
class ExceptionMappingUtil {

    static Response createErrorResponse(final Response.Status status, final String errorMessage, final String details) {
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorMessage(status.getStatusCode(), errorMessage, details))
                .build();
    }

}
