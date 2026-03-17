package com.kejiahp.musicx.util.exceptions.authorization;

import com.kejiahp.musicx.util.exceptions.DomainException;

public class UnauthorizedException extends DomainException {
    public UnauthorizedException() {
        super("Insufficient authentication, please reauthenticate and try again", "UNAUTHORIZED_REQUEST", "");
    }

    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED_REQUEST", "");
    }
}
