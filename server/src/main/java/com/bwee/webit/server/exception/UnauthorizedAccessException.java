package com.bwee.webit.server.exception;

import javax.servlet.http.HttpServletRequest;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(final HttpServletRequest req) {
        super("Unauthorized Request: " + req.getMethod() + " " + req.getRequestURI());
    }
}
