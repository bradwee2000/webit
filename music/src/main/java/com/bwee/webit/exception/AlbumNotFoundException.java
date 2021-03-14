package com.bwee.webit.exception;

import com.bwee.webit.exception.ContentNotFoundException;

public class AlbumNotFoundException extends ContentNotFoundException {

    private static final String SOURCE = "album";

    public AlbumNotFoundException(String id) {
        super(id, SOURCE);
    }
}
