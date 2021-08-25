package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.MusicUserEntity;
import com.bwee.webit.model.MusicUser;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MusicUserDbService extends AbstractDbService<MusicUser, MusicUserEntity, String> {

    private final CassandraOperations cassandra;

    @Autowired
    public MusicUserDbService(final CassandraOperations cassandra) {
        super(cassandra, MusicUserEntity.class);
        this.cassandra = cassandra;
    }

    public MusicUser findByIdOrCreate(final String id) {
        return findById(id).orElseGet(() -> save(new MusicUser().setId(id)));
    }

    public void updateCurrentTrackIndex(final MusicUser model) {
        updateColumn(model.getId(), "currentTrackIndex", model.getCurrentTrackIndex());
    }

    public void updateShuffle(final MusicUser model) {
        updateColumns(model.getId(),
                "isShuffle", model.isShuffle(),
                "trackIdQueue", model.getTrackIdQueue(),
                "currentTrackIndex", model.getCurrentTrackIndex()
                );
    }

    public void updateLoop(final MusicUser model) {
        updateColumn(model.getId(), "isLoop", model.isLoop());
    }

    public void update(final Update update) {
        final Map<String, Object> values = new HashMap<>();

        if (update.name() != null) {
            values.put("name", update.name());
        }
        if (update.trackIdQueue() != null) {
            values.put("trackIdQueue", update.trackIdQueue());
        }
        if (update.currentTrackIndex() != null) {
            values.put("currentTrackIndex", update.currentTrackIndex());
        }
        if (update.isShuffle() != null) {
            values.put("isShuffle", update.isShuffle());
        }
        if (update.isLoop() != null) {
            values.put("isLoop", update.isLoop());
        }
        if (update.isPlaying() != null) {
            values.put("isPlaying", update.isPlaying());
        }
        if (!values.isEmpty()) {
            updateColumns(update.id(), values);
        }
    }

    @Override
    public MusicUser toModel(final MusicUserEntity entity) {
        return entity.toModel();
    }

    @Override
    public MusicUserEntity toEntity(final MusicUser model) {
        return new MusicUserEntity(model);
    }

    @Data
    @Accessors(fluent = true)
    public static class Update {
        public static Update of(final String userId) {
            return new Update(userId);
        }

        private final String id;
        private String name;
        private List<String> trackIdQueue;
        private Integer currentTrackIndex;
        private Boolean isShuffle;
        private Boolean isLoop;
        private Boolean isPlaying;
        private String selectedDeviceId;

        public Update(String id) {
            this.id = id;
        }
    }
}
