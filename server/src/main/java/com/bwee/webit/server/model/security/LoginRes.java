package com.bwee.webit.server.model.security;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginRes {

    private String authToken;
}
