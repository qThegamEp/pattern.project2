package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.ErrorType;

public class OpenApiException extends GeneralServiceException {

    public OpenApiException(Throwable cause, ErrorType errorType) {
        super(cause, errorType);
    }
}