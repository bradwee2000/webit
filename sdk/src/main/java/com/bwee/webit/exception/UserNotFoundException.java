package com.bwee.webit.exception;

public class UserNotFoundException extends NotFoundException {

    public static UserNotFoundException ofId(final String id) {
        return new UserNotFoundException("User is not found. id=" + id);
    }

    public static UserNotFoundException ofUsername(final String username) {
        return new UserNotFoundException("User is not found. username=" + username);
    }

    private UserNotFoundException(final String msg) {
        super(msg);
    }
}
