package com.bwee.webit.exception;

public class AlbumContentNotFoundException extends ContentNotFoundException {

    private static final String SOURCE = "album";

    public AlbumContentNotFoundException(String id) {
        super(id, SOURCE);
    }
}
