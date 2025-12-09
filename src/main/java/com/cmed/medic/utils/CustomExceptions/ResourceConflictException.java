package com.cmed.medic.utils.CustomExceptions;

public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException() {
        super("Resource Conflict");
    }

    public ResourceConflictException(String message) {
        super(message);
    }
}
