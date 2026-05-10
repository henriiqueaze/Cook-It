package com.p5Project.cookIt.exceptions;

public class EmailAlreadyInUseException extends BadRequestException {
    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}

