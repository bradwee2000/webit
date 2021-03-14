package com.bwee.webit.server.exception;

import com.bwee.webit.exception.InvalidTokenException;
import com.bwee.webit.exception.NotFoundException;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity handleInvalidTokenException(InvalidTokenException e) {
        final Error error = new Error().setError(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleAllOtherExceptions(Exception e) {
        final Error error = new Error().setError(e.getMessage());
        log.error("Error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


    @Data
    @Accessors(chain = true)
    public static class Error {
        private String error;
    }
}
