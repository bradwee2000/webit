package com.bwee.webit.server.exception;

import com.bwee.webit.exception.NotFoundException;
import com.bwee.webit.heos.sddp.Response;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @Data
    @Accessors(chain = true)
    public static class Error {
        private String error;
    }
}
