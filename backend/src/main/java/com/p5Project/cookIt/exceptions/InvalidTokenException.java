package com.p5Project.cookIt.exceptions;

public class InvalidTokenException extends BadRequestException {
    public InvalidTokenException(String message) {
        super(message);
    }
}

