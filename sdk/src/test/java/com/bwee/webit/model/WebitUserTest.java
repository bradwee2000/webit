package com.bwee.webit.model;

import com.bwee.webit.datasource.entity.WebitUserEntity;
import com.bwee.webit.model.WebitUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WebitUserTest {

    private WebitUser user;

    @BeforeEach
    public void before() {
        user = new WebitUser()
                .setId("123")
                .setName("John")
                .setUsername("john123")
                .setPassword("XYZ")
                .setRoles(List.of("ADMIN", "SUP"));
    }

    @Test
    public void testCreate_shouldCopyProperties() {
        final WebitUserEntity entity = new WebitUserEntity(user);
        assertThat(entity.getId()).isEqualTo(user.getId());
        assertThat(entity.getName()).isEqualTo(user.getName());
        assertThat(entity.getUsername()).isEqualTo(user.getUsername());
        assertThat(entity.getPassword()).isEqualTo(user.getPassword());
        assertThat(entity.getRoles()).isEqualTo(user.getRoles());
    }

    @Test
    public void testToModel_shouldReturnModelEqualToInputModel() {
        assertThat(new WebitUserEntity(user).toModel()).isEqualTo(user);
    }
}