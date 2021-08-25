package com.bwee.webit.stats.db;

import com.bwee.webit.datasource.entity.UserTrackPlayDailyStatEntity;
import com.bwee.webit.stats.model.StatDuration;
import com.bwee.webit.stats.model.UserTrackStat;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserTrackPlayDailyStatEntityTest {

    @Test
    public void testToModel_shouldReturnEqualModel() {
        final UserTrackStat stat = new UserTrackStat()
                .setCount(1)
                .setTime(LocalDateTime.now())
                .setDuration(StatDuration.Daily)
                .setTrackId("ABC")
                .setUserId("USER_1");

        assertThat(new UserTrackPlayDailyStatEntity(stat).toModel()).isEqualTo(stat);
    }
}