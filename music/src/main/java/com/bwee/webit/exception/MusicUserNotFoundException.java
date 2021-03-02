package com.bwee.webit.exception;

import com.bwee.webit.core.exception.NotFoundException;

public class MusicUserNotFoundException extends NotFoundException {

    public MusicUserNotFoundException(String id) {
        super("Music user is not found. id=" + id);
    }
}
