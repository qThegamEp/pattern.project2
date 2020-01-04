package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class JsonConverterServiceRuntimeException extends GeneralServiceRuntimeException {

    public JsonConverterServiceRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
