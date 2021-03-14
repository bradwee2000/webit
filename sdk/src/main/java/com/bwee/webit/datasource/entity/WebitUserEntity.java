package com.bwee.webit.datasource.entity;

import com.bwee.webit.model.WebitUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.*;

@Table("webitUser")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class WebitUserEntity implements Entity<WebitUserEntity> {

    @PrimaryKey
    private String id;

    private String name;

    @Indexed
    private String username;

    private String password;

    @CassandraType(type = SET, typeArguments = TEXT)
    private List<String> roles;

    public WebitUserEntity(final WebitUser user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }

    public WebitUser toModel() {
        return new WebitUser()
                .setId(id)
                .setName(name)
                .setUsername(username)
                .setPassword(password)
                .setRoles(Optional.ofNullable(roles).orElse(Collections.emptyList()));
    }
}
