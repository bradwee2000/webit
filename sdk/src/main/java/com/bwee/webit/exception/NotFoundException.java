package com.bwee.webit.exception;

public abstract class NotFoundException extends RuntimeException {

    public NotFoundException(final String msg) {
        super(msg);
    }
}
