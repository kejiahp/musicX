package com.kejiahp.musicx.util.exceptions.authorization;

import com.kejiahp.musicx.util.exceptions.DomainException;

public class ForbiddenException extends DomainException {
    public ForbiddenException() {
        super("You are not allowed to perform this action (operation)", "FORBIDDEN_REQUEST", "");
    }

    public ForbiddenException(String message) {
        super(message, "FORBIDDEN_REQUEST", "");
    }
}
