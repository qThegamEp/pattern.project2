package com.qthegamep.pattern.project2.exception.compile;

import com.qthegamep.pattern.project2.model.container.Error;

public class OpenApiControllerException extends GeneralServiceException {

    public OpenApiControllerException(Throwable cause, Error error) {
        super(cause, error);
    }
}
