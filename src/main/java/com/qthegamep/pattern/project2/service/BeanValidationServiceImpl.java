package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.exception.runtime.ValidationServiceRuntimeException;
import com.qthegamep.pattern.project2.model.container.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.Validator;
import java.util.stream.Collectors;

public class BeanValidationServiceImpl implements BeanValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(BeanValidationServiceImpl.class);

    private Validator validator;

    @Inject
    public BeanValidationServiceImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public <T> void validateBean(T object) {
        if (object != null) {
            String error = validator.validate(object)
                    .stream()
                    .map(value -> String.format("'%s' %s", value.getPropertyPath(), value.getMessage()))
                    .collect(Collectors.joining("; "));
            LOG.debug("Validate bean error: {}", error);
            if (!error.isEmpty()) {
                throw new ValidationServiceRuntimeException(error, Error.VALIDATION_ERROR);
            }
        }
    }
}
