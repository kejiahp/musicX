package com.kejiahp.musicx.util.exceptions.validation;

import com.kejiahp.musicx.util.exceptions.DomainException;

public class RecordNotFound extends DomainException {
    public RecordNotFound(String message, String fieldName) {
        super(message, "NOT_FOUND", fieldName);
    }
}
