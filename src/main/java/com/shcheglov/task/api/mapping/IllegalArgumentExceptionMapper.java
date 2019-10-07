package com.shcheglov.task.api.mapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static com.shcheglov.task.api.mapping.ExceptionMappingUtil.createErrorResponse;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * @author Anton
 */
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    private static final Logger LOG = LoggerFactory.getLogger(IllegalArgumentExceptionMapper.class.getName());

    @Override
    public Response toResponse(IllegalArgumentException e) {
        LOG.error("IllegalArgumentException: ", e);
        return createErrorResponse(BAD_REQUEST, BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }

}
