package com.qthegamep.pattern.project2.mapper;

import com.qthegamep.pattern.project2.dto.ErrorResponseDTO;
import com.qthegamep.pattern.project2.exception.ServiceException;
import com.qthegamep.pattern.project2.metric.Metrics;
import com.qthegamep.pattern.project2.model.ErrorType;
import com.qthegamep.pattern.project2.service.ErrorResponseBuilderService;
import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = LoggerFactory.getLogger(GeneralExceptionMapper.class);

    private HttpHeaders httpHeaders;
    private ErrorResponseBuilderService errorResponseBuilderService;

    @Inject
    public GeneralExceptionMapper(@Context HttpHeaders httpHeaders, ErrorResponseBuilderService errorResponseBuilderService) {
        this.httpHeaders = httpHeaders;
        this.errorResponseBuilderService = errorResponseBuilderService;
    }

    @Override
    public Response toResponse(Exception exception) {
        ErrorType errorType = getErrorType(exception);
        List<Locale> requestLocales = httpHeaders.getAcceptableLanguages();
        String requestId = httpHeaders.getHeaderString(Constants.REQUEST_ID_HEADER.getValue());
        LOG.error("Error. RequestId: {}", requestId, exception);
        ErrorResponseDTO errorResponseDTO = errorResponseBuilderService.buildResponse(errorType, requestLocales, requestId);
        errorResponseDTO.setErrorMessage(errorResponseDTO.getErrorMessage() + " Request ID: " + requestId);
        registerMetrics(errorType, requestId);
        return buildResponse(errorResponseDTO);
    }

    private ErrorType getErrorType(Exception exception) {
        if (exception instanceof ServiceException) {
            return ((ServiceException) exception).getErrorType();
        } else if (exception instanceof ValidationException) {
            return ErrorType.INVALID_REQUEST_RESPONSE_ERROR;
        } else if (exception instanceof NotFoundException) {
            return ErrorType.PAGE_NOT_FOUND_ERROR;
        } else {
            return ErrorType.INTERNAL_ERROR;
        }
    }

    private void registerMetrics(ErrorType errorType, String requestId) {
        String errorCode = String.valueOf(errorType.getErrorCode());
        long errorCount = Metrics.ERROR_TYPES_METRIC.get(errorCode).incrementAndGet();
        LOG.debug("Error code: {} Error count: {} RequestId: {}", errorCode, errorCount, requestId);
    }

    private Response buildResponse(ErrorResponseDTO errorResponseDTO) {
        String contentType = getContentType(httpHeaders);
        if (errorResponseDTO.getErrorCode() == ErrorType.PAGE_NOT_FOUND_ERROR.getErrorCode()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .entity(errorResponseDTO)
                    .build();
        }
    }

    private String getContentType(HttpHeaders httpHeaders) {
        String contentType = httpHeaders.getHeaderString(HttpHeaders.CONTENT_TYPE);
        LOG.debug("Content-Type: {}", contentType);
        if (contentType == null || contentType.isEmpty()) {
            contentType = MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.displayName()).toString();
            LOG.debug("New Content-Type: {}", contentType);
        }
        return contentType;
    }
}
