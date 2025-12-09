package com.cmed.medic.utils.CustomExceptions;

public class UnauthorizedResourceException extends RuntimeException {

    public UnauthorizedResourceException() {
        super("You do not have permission to access this resource");
    }

    public UnauthorizedResourceException(String message) {
        super(message);
    }
}