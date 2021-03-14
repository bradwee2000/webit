package com.bwee.webit.exception;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException() {

        super("Username already exists.");
    }
}
