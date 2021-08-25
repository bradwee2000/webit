package com.bwee.webit.auth;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
public class AuthUser {

    private final String token;
    private final String userId;
    private final String name;
    private final List<String> roles;

    @Data
    @Accessors(chain = true)
    public static class LoginRes {
        private String token;
        private String name;
    }
}
