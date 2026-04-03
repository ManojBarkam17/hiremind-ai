package com.hiremind.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ResourceNotFoundException forEntity(String entityName, String identifier) {
        return new ResourceNotFoundException(
            String.format("%s with identifier '%s' not found", entityName, identifier)
        );
    }
}
