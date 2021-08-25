package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.YoutubeEntity;
import com.bwee.webit.model.YoutubeVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class YoutubeDbService extends AbstractDbService<YoutubeVideo, YoutubeEntity, String> {

    private final CassandraOperations cassandra;

    @Autowired
    public YoutubeDbService(final CassandraOperations cassandra) {
        super(cassandra, YoutubeEntity.class);
        this.cassandra = cassandra;
    }

    @Override
    public YoutubeVideo toModel(YoutubeEntity entity) {
        return entity.toModel();
    }

    @Override
    public YoutubeEntity toEntity(YoutubeVideo model) {
        return new YoutubeEntity(model);
    }
}
