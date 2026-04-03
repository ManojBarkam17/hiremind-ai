package com.hiremind.common.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<String, String> fieldErrors = new HashMap<>();

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        if (fieldErrors != null) {
            this.fieldErrors.putAll(fieldErrors);
        }
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void addFieldError(String field, String error) {
        fieldErrors.put(field, error);
    }
}
