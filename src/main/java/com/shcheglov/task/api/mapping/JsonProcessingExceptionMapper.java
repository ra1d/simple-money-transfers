package com.shcheglov.task.api.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static com.shcheglov.task.api.mapping.ExceptionMappingUtil.createErrorResponse;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * @author Anton
 */
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {

    private static final Logger LOG = LoggerFactory.getLogger(JsonProcessingExceptionMapper.class.getName());

    @Override
    public Response toResponse(JsonProcessingException e) {
        LOG.error("JsonProcessingException: ", e);
        return createErrorResponse(BAD_REQUEST, "Failed parsing the request entity!", e.getMessage());
    }

}
