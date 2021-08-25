package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.AlbumEntity;
import com.bwee.webit.model.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import static java.util.Comparator.comparing;

@Service
public class AlbumDbService extends AbstractDbService<Album, AlbumEntity, String> {

    private final CassandraOperations cassandra;

    @Autowired
    public AlbumDbService(final CassandraOperations cassandra) {
        super(cassandra, AlbumEntity.class);
        this.cassandra = cassandra;
    }

    @Override
    public Album toModel(final AlbumEntity entity) {
        return entity.toModel();
    }

    @Override
    public AlbumEntity toEntity(final Album model) {
        return new AlbumEntity(model);
    }

}
