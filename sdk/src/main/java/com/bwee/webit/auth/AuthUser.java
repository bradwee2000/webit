package com.bwee.webit.auth;

import lombok.Data;

@Data
public class AuthUser {

    private final String token;
    private final String userId;

    public AuthUser(final String token, final String userId) {
        this.token = token;
        this.userId = userId;
    }
}
