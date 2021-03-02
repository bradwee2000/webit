package com.bwee.webit.exception;

import com.bwee.webit.core.exception.ContentNotFoundException;

public class AlbumContentNotFoundException extends ContentNotFoundException {

    private static final String SOURCE = "album";

    public AlbumContentNotFoundException(String id) {
        super(id, SOURCE);
    }
}
