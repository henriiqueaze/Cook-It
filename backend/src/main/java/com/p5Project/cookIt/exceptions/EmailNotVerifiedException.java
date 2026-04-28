package com.p5Project.cookIt.exceptions;

public class EmailNotVerifiedException extends ForbiddenOperationException {
    public EmailNotVerifiedException(String message) {
        super(message);
    }
}

