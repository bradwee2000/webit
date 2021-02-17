package com.bwee.webit.exception;

public class MusicUserNotFoundException extends NotFoundException {

    public MusicUserNotFoundException(String id) {
        super("Music user is not found. id=" + id);
    }
}
