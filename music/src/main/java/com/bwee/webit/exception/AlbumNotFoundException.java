package com.bwee.webit.exception;

public class AlbumNotFoundException extends ContentNotFoundException {

    private static final String SOURCE = "album";

    public AlbumNotFoundException(String id) {
        super(id, SOURCE);
    }
}
