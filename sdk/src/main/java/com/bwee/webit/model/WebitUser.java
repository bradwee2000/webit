package com.bwee.webit.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class WebitUser {

    private String id;
    private String name;
    private String username;
    private String password;
    private List<String> roles;
}
