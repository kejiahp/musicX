package com.kejiahp.musicx.util.exceptions.validation;

import com.kejiahp.musicx.util.exceptions.DomainException;

public class InvalidEmailException extends DomainException {
    public InvalidEmailException() {
        super("Invalid email", "INVALID_EMAIL", "email");
    }

    public InvalidEmailException(String fieldName) {
        super("Invalid email", "INVALID_EMAIL", fieldName);
    }
}
