package com.bwee.webit.heos.model;

import lombok.Getter;

@Getter
public enum AddCriteria {

    PLAY_NOW(1),
    PLAY_NEXT(2),
    ADD_TO_END(3),
    REPLACE_AND_PLAY(4);

    int code;

    AddCriteria(int code) {
        this.code = code;
    }
}
