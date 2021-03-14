package com.bwee.webit.exception;

import com.bwee.webit.exception.ContentNotFoundException;

public class TrackNotFoundException extends ContentNotFoundException {

    private static final String SOURCE = "music";

    public TrackNotFoundException(String id) {
        super(id, SOURCE);
    }
}
