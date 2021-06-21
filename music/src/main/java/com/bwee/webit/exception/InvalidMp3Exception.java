package com.bwee.webit.exception;

import lombok.Getter;

@Getter
public class InvalidMp3Exception extends RuntimeException {

    private static final String MSG = "Failed to read MP3: ";

    private final String filename;
    private final String error;

    public InvalidMp3Exception(String filename, String error) {
        super(MSG + filename + ". " + error);
        this.filename = filename;
        this.error = error;
    }
}
