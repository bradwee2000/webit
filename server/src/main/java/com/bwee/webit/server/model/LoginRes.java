package com.bwee.webit.server.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginRes {

    private String accessToken;
    private String name;
}
