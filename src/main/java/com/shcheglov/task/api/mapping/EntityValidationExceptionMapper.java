package com.shcheglov.task.api.mapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static com.shcheglov.task.api.mapping.ExceptionMappingUtil.createErrorResponse;
import static java.util.stream.Collectors.joining;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * @author Anton
 */
public class EntityValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    private static final Logger LOG = LoggerFactory.getLogger(EntityValidationExceptionMapper.class.getName());

    @Override
    public Response toResponse(ValidationException e) {
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException cvex = (ConstraintViolationException) e;
            LOG.error("ConstraintViolationException: ", cvex);
            return createErrorResponse(BAD_REQUEST, cvex.getConstraintViolations().stream()
                    .map(constrViolation -> getFieldName(constrViolation) + ": " + constrViolation.getMessage())
                    .collect(joining(", ")), null);
        }
        LOG.error("Non-constraint related validation exception: ", e);
        return createErrorResponse(BAD_REQUEST, "Unexpected validation exception!", e.getMessage());
    }

    private static String getFieldName(ConstraintViolation constraintViolation) {
        String[] tokenizedPath = constraintViolation.getPropertyPath().toString().split("\\.");
        return constraintViolation.getLeafBean()
                .getClass()
                .getSimpleName() + "." + tokenizedPath[tokenizedPath.length - 1];
    }

}
