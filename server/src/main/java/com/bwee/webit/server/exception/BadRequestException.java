package com.bwee.webit.server.exception;

public class BadRequestException extends IllegalArgumentException {

    public BadRequestException(final String message) {
        super(message);
    }
}
