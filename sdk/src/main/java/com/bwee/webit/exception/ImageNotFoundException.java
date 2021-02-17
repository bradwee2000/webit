package com.bwee.webit.exception;

public class ImageNotFoundException extends ContentNotFoundException {

    private static final String SOURCE = "image";

    public ImageNotFoundException(final String id) {
        super(id, SOURCE);
    }
}
