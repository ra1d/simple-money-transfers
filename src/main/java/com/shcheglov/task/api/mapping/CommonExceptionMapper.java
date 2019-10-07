package com.shcheglov.task.api.mapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static com.shcheglov.task.api.mapping.ExceptionMappingUtil.createErrorResponse;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

/**
 * @author Anton
 */
public class CommonExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = LoggerFactory.getLogger(CommonExceptionMapper.class.getName());

    @Override
    public Response toResponse(Exception e) {
        if (e instanceof WebApplicationException) {
            final WebApplicationException wae = (WebApplicationException) e;
            LOG.error("WebApplicationException: ", wae);
            return createErrorResponse(Response.Status.fromStatusCode(wae.getResponse().getStatus()), wae.getMessage(), null);
        }
        LOG.error("Unexpected exception: ", e);
        return createErrorResponse(INTERNAL_SERVER_ERROR, "Unexpected exception!", e.getMessage());
    }

}
