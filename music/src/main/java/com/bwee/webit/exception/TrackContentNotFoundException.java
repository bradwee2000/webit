package com.bwee.webit.exception;

public class TrackContentNotFoundException extends ContentNotFoundException {

    private static final String SOURCE = "music";

    public TrackContentNotFoundException(String id) {
        super(id, SOURCE);
    }
}
