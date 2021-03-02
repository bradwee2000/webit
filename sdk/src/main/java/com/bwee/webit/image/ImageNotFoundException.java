package com.bwee.webit.image;

import com.bwee.webit.core.exception.ContentNotFoundException;

public class ImageNotFoundException extends ContentNotFoundException {

    private static final String SOURCE = "image";

    public ImageNotFoundException(final String id) {
        super(id, SOURCE);
    }
}
