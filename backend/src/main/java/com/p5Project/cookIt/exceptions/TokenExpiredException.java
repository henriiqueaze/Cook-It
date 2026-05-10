package com.p5Project.cookIt.exceptions;

public class TokenExpiredException extends BadRequestException {
    public TokenExpiredException(String message) {
        super(message);
    }
}

