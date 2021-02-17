package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.MusicUserEntity;
import com.bwee.webit.model.MusicUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import static org.springframework.data.cassandra.core.query.Criteria.where;
import static org.springframework.data.cassandra.core.query.Query.query;

@Service
public class MusicUserDbService extends AbstractDbService<MusicUser, MusicUserEntity> {

    private final CassandraOperations cassandra;

    @Autowired
    public MusicUserDbService(final CassandraOperations cassandra) {
        super(cassandra, MusicUserEntity.class);
        this.cassandra = cassandra;
    }

    public void updateCurrentTrackNum(final MusicUser model) {
        updateColumn(model.getId(), "currentTrackNum", model.getCurrentTrackIndex());
    }

    public void updateTrackIds(final MusicUser model) {
        updateColumn(model.getId(), "trackIdQueue", model.getTrackIdQueue());
    }

    public void updateShuffle(final MusicUser model) {
        updateColumns(model.getId(),
                "isShuffle", model.isShuffle(),
                "trackIdQueue", model.getTrackIdQueue(),
                "currentTrackNum", model.getCurrentTrackIndex()
                );
    }

    public void updateLoop(final MusicUser model) {
        updateColumn(model.getId(), "isLoop", model.isLoop());
    }

    public void updatePlaying(final MusicUser model) {
        updateColumn(model.getId(), "isPlaying", model.isPlaying());
    }

    @Override
    public MusicUser toModel(final MusicUserEntity entity) {
        return entity.toModel();
    }

    @Override
    public MusicUserEntity toEntity(final MusicUser model) {
        return new MusicUserEntity(model);
    }
}
