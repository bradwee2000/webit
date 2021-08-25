package com.bwee.webit.stats.db;

import com.bwee.webit.datasource.AbstractDbService;
import com.bwee.webit.datasource.entity.UserTrackPlayDailyStatEntity;
import com.bwee.webit.stats.model.UserTrackStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.cassandra.core.query.Criteria.where;

@Service
public class UserTrackStatDailyDbService extends AbstractDbService<UserTrackStat, UserTrackPlayDailyStatEntity, UserTrackPlayDailyStatEntity.Id> {

    private final CassandraOperations cassandra;
    private final Clock clock;

    @Autowired
    public UserTrackStatDailyDbService(final CassandraOperations cassandra, final Clock clock) {
        super(cassandra, UserTrackPlayDailyStatEntity.class);
        this.cassandra = cassandra;
        this.clock = clock;
    }

    public Optional<UserTrackStat> findById(final String userId, final LocalDateTime time, final String trackId) {
        if (userId == null || time == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(
                cassandra.selectOneById(UserTrackPlayDailyStatEntity.Id.of(userId, time, trackId), UserTrackPlayDailyStatEntity.class))
                .map(e -> toModel(e));
    }

    public List<UserTrackStat> findByUserId(final String userId, final int totalDays) {
        final LocalDateTime afterTime = LocalDateTime.now(clock).minusDays(totalDays);

        final Query query = Query.query(where("userid").is(userId))
                .and(where("time").gte(afterTime));

        return cassandra.select(query, UserTrackPlayDailyStatEntity.class).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public UserTrackStat toModel(final UserTrackPlayDailyStatEntity entity) {
        return entity.toModel();
    }

    @Override
    public UserTrackPlayDailyStatEntity toEntity(final UserTrackStat model) {
        return new UserTrackPlayDailyStatEntity(model);
    }
}
