package com.bwee.webit.stats.model;

import lombok.Getter;

@Getter
public enum UserAction {

    PressPlay(1),
    PressPause(2),
    ;

    public static UserAction findByCode(final Integer code) {
        for (UserAction action : UserAction.values()) {
            if (action.code == code) {
                return action;
            }
        }
        throw new IllegalStateException("UserAction code not found: " + code);
    }

    int code;

    UserAction(int code) {
        this.code = code;
    }
}
