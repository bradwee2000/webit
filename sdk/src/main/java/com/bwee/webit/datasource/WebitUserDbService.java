package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.WebitUserEntity;
import com.bwee.webit.model.WebitUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.cassandra.core.query.Criteria.where;

@Service
public class WebitUserDbService extends AbstractDbService<WebitUser, WebitUserEntity> {

    private final CassandraOperations cassandra;

    @Autowired
    public WebitUserDbService(final CassandraOperations cassandra) {
        super(cassandra, WebitUserEntity.class);
        this.cassandra = cassandra;
    }

    public Optional<WebitUser> findByUsername(final String username) {
        final Query query = Query.query(where("username").is(username));
        return Optional.ofNullable(cassandra.selectOne(query, WebitUserEntity.class))
                .map(e -> e.toModel());
    }

    @Override
    public WebitUser toModel(final WebitUserEntity entity) {
        return entity.toModel();
    }

    @Override
    public WebitUserEntity toEntity(final WebitUser model) {
        return new WebitUserEntity(model);
    }
}
