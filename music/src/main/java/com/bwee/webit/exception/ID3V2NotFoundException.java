package com.bwee.webit.exception;

import lombok.Getter;

@Getter
public class ID3V2NotFoundException extends RuntimeException {

    private static final String MSG = "ID3V2 data not found for MP3 file: ";

    private final String filename;

    public ID3V2NotFoundException(String filename) {
        super(MSG + filename);
        this.filename = filename;
    }
}
