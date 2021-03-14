package com.bwee.webit.server.model;

import com.bwee.webit.model.WebitUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class WebitUserRes {

    private String id;
    private String name;
    private List<String> roles;

    public WebitUserRes(final WebitUser user) {
        this.id = user.getId();
        this.name = user.getName();
        this.roles = user.getRoles();
    }
}
