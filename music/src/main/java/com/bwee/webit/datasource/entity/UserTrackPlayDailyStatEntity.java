package com.bwee.webit.datasource.entity;

import com.bwee.webit.stats.model.StatDuration;
import com.bwee.webit.stats.model.UserTrackStat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDateTime;

//@Table("user_track_play_daily_stat")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class UserTrackPlayDailyStatEntity implements Entity<UserTrackPlayDailyStatEntity, UserTrackPlayDailyStatEntity.Id> {

    @PrimaryKey
    private Id id;

    private Integer count;

    public UserTrackPlayDailyStatEntity(final UserTrackStat stat) {
        this.id = new Id().setUserId(stat.getUserId()).setTime(stat.getTime()).setTrackId(stat.getTrackId());
        this.count = stat.getCount();
    }

    public UserTrackStat toModel() {
        return new UserTrackStat()
                .setUserId(id.getUserId())
                .setTrackId(id.getTrackId())
                .setCount(count)
                .setTime(id.getTime())
                .setDuration(StatDuration.Daily);
    }

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public UserTrackPlayDailyStatEntity setId(final Id id) {
        this.id = id;
        return this;
    }

    @Data
    @PrimaryKeyClass
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Id implements Serializable {

        public static final Id of(final String userId, final LocalDateTime time, final String trackId) {
            return new Id(userId, time, trackId);
        }

        @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private String userId;

        @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private LocalDateTime time;

        @PrimaryKeyColumn(ordinal = 2, type = PrimaryKeyType.CLUSTERED)
        private String trackId;
    }
}
