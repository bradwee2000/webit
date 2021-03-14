package com.bwee.webit.exception;

public class InvalidTokenException extends RuntimeException {

    public static InvalidTokenException expired() {
        return new InvalidTokenException(Cause.EXPIRED);
    }

    public static InvalidTokenException malformed() {
        return new InvalidTokenException(Cause.MALFORMED);
    }

    public static InvalidTokenException unsupported() {
        return new InvalidTokenException(Cause.UNSUPPORTED);
    }

    public static InvalidTokenException invalidSignature() {
        return new InvalidTokenException(Cause.INVALID_SIGNATURE);
    }

    public enum Cause {
        EXPIRED, MALFORMED, UNSUPPORTED, INVALID_SIGNATURE
    }

    private InvalidTokenException(final Cause cause) {
        super("Token is invalid.");
    }
}
