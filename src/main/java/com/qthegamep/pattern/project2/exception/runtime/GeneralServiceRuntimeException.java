package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.exception.ServiceException;
import com.qthegamep.pattern.project2.model.container.Error;

abstract class GeneralServiceRuntimeException extends RuntimeException implements ServiceException {

    private final Error error;

    GeneralServiceRuntimeException(Error error) {
        this.error = error;
    }

    GeneralServiceRuntimeException(Throwable cause, Error error) {
        super(cause);
        this.error = error;
    }

    public GeneralServiceRuntimeException(String message, Error error) {
        super(message);
        this.error = error;
    }

    @Override
    public Error getError() {
        return error;
    }
}