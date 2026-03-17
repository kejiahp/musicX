package com.kejiahp.musicx.util.exceptions;

public abstract class DomainException extends RuntimeException {
    private final String code;
    private final String field;

    protected DomainException(String message, String code, String field) {
        super(message);
        this.code = code;
        this.field = field;
    }

    public String getCode() {
        return this.code;
    }

    public String getField() {
        return this.field;
    }
}
