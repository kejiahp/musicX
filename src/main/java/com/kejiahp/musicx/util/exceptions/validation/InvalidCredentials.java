package com.kejiahp.musicx.util.exceptions.validation;

import com.kejiahp.musicx.util.exceptions.DomainException;

public class InvalidCredentials extends DomainException {
    public InvalidCredentials() {
        super("Invalid Credentials", "INVALID_CREDENTIALS", "");
    }
}
