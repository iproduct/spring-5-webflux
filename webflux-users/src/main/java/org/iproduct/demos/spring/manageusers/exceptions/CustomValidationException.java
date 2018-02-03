package org.iproduct.demos.spring.manageusers.exceptions;

import org.springframework.validation.ObjectError;

import java.util.List;

public class CustomValidationException extends RuntimeException {
    private List<ObjectError> errors;
    public CustomValidationException(List<ObjectError> allErrors) {
        super("Validation errors");
        errors = allErrors;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }
}
