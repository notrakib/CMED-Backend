package com.cmed.medic.utils.CustomExceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Not Found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
