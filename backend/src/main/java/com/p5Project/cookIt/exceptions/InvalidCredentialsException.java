package com.p5Project.cookIt.exceptions;

public class InvalidCredentialsException extends BadRequestException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}

