package com.cmed.medic.utils.CustomExceptions;

public class WrongCredentialsException extends RuntimeException {

    public WrongCredentialsException() {
        super("Invalid Credentials");
    }

    public WrongCredentialsException(String message) {
        super(message);
    }
}
