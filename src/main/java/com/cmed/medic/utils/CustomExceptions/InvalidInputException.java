package com.cmed.medic.utils.CustomExceptions;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException() {
        super("Provide Valid Data");
    }

    public InvalidInputException(String message) {
        super(message);
    }
}