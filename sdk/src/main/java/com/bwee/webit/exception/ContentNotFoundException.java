package com.bwee.webit.exception;

public class ContentNotFoundException extends NotFoundException {

    public ContentNotFoundException(final String id, final String source) {
        super("Content is not found. id=" + id + ", source=" + source);
    }
}
