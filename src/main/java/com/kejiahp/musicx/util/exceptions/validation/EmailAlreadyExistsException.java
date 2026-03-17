package com.kejiahp.musicx.util.exceptions.validation;

import com.kejiahp.musicx.util.exceptions.DomainException;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException() {
        super("Email already exists", "EMAIL_EXISTS", "email");
    }

    public EmailAlreadyExistsException(String fieldName) {
        super("Email already exists", "EMAIL_EXISTS", fieldName);
    }
}
