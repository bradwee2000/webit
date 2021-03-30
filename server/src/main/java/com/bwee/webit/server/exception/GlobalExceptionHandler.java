package com.bwee.webit.server.exception;

import com.bwee.webit.exception.InvalidCredentialsException;
import com.bwee.webit.exception.InvalidTokenException;
import com.bwee.webit.exception.NotFoundException;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFound(RuntimeException e) {
        final Error error = new Error().setError(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity handleUnauthorizedRequest(RuntimeException e) {
        final Error error = new Error().setError(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity handleInvalidCredentialsException(final InvalidCredentialsException e) {
        final Error error = new Error().setError(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        final Error error = new Error().setError(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequestException(final BadRequestException e) {
        final Error error = new Error().setError(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity exceptionHandler(IOException e, HttpServletRequest request) {
        if (StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(e), "Broken pipe")) {   //(2)
            return null;        //(2)	socket is closed, cannot return any response
        } else {
            final Error error = new Error().setError(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity handleAllOtherExceptions(final Exception e) {
//        final Error error = new Error().setError(e.getMessage());
//        log.error("Server Error", e);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }

    @Data
    @Accessors(chain = true)
    public static class Error {
        private String error;
    }
}
