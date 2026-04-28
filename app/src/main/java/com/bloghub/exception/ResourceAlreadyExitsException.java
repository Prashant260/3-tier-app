package com.bloghub.exception;

public class ResourceAlreadyExitsException extends RuntimeException {
    public ResourceAlreadyExitsException(String message) {
        super(message);
    }
}
