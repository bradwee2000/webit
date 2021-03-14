package com.bwee.webit.heos.model;

import lombok.Getter;

@Getter
public enum PlayState {
    PLAY("play"),
    PAUSE("pause"),
    STOP("stop");

    String code;

    PlayState(String code) {
        this.code = code;
    }
}
