package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.TrackEntity;
import com.bwee.webit.model.Track;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class TrackDbService extends AbstractDbService<Track, TrackEntity> {

    private final CassandraOperations cassandra;

    @Autowired
    public TrackDbService(final CassandraOperations cassandra) {
        super(cassandra, TrackEntity.class);
        this.cassandra = cassandra;
    }

    @Override
    protected TrackEntity merge(TrackEntity existingEntity, TrackEntity newEntity) {
        if (newEntity.equals(existingEntity)) {
            return newEntity;
        }

        final List<String> genre = Stream.concat(existingEntity.getGenre().stream(), newEntity.getGenre().stream())
                .distinct().collect(toList());
        final List<String> tags = Stream.concat(existingEntity.getTags().stream(), newEntity.getTags().stream())
                .distinct().collect(toList());

        return TrackEntity.copyOf(newEntity).setGenre(genre).setTags(tags);
    }

    @Override
    public Track toModel(final TrackEntity entity) {
        return entity.toModel();
    }

    @Override
    public TrackEntity toEntity(final Track model) {
        return new TrackEntity(model);
    }
}
