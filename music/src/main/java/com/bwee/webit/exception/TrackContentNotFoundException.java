package com.bwee.webit.exception;

import com.bwee.webit.core.exception.ContentNotFoundException;

public class TrackContentNotFoundException extends ContentNotFoundException {

    private static final String SOURCE = "music";

    public TrackContentNotFoundException(String id) {
        super(id, SOURCE);
    }
}
