package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.Entity;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.cassandra.core.query.Update;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.cassandra.core.query.Criteria.where;
import static org.springframework.data.cassandra.core.query.Query.query;
import static org.springframework.data.cassandra.core.query.Update.update;

@Slf4j
public abstract class AbstractDbService<T, E extends Entity<E, K>, K> implements DbService<T, E, K> {

    private static final int BATCH_SIZE = 30;

    private final CassandraOperations cassandra;
    private final Class<E> clazz;
    private final int defaultPageSize = 20;

    public AbstractDbService(final CassandraOperations cassandra,
                             final Class<E> clazz) {
        this.cassandra = cassandra;
        this.clazz = clazz;
    }

    @Override
    public T save(final T model) {
        final E entity = toEntity(model);
        return toModel(cassandra.update(entity));
    }



    @Override
    public List<T> saveAll(final Collection<T> models) {
        if (models.isEmpty()) {
            return emptyList();
        }

        final List<E> entities = models.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        Lists.partition(entities, BATCH_SIZE).stream()
                .forEach(batch -> cassandra.batchOps().update(batch).execute());

        return entities.stream().map(this::toModel).collect(toList());
    }

    public Optional<T> findById(final K id) {
        if (id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(cassandra.selectOneById(id, clazz))
                .map(e -> toModel(e));
    }

    public List<T> findByIdsSorted(final List<K> ids) {
        if (ids.isEmpty()) {
            return emptyList();
        }

        return cassandra.select(query(where("id").in(ids)), clazz).stream()
                .sorted(Ordering.explicit(ids).onResultOf(E::getId))
                .map(entity -> toModel(entity))
                .collect(toList());
    }

    public List<T> findByIds(final Collection<K> ids) {
        if (ids == null || ids.isEmpty()) {
            return emptyList();
        }
        return cassandra.select(query(where("id").in(ids)), clazz).stream()
                .map(entity -> toModel(entity))
                .collect(toList());
    }

    public Slice<T> findAll() {
        return findAll(defaultPageSize);
    }

    public Slice<T> findAll(int size) {
        return findAll(CassandraPageRequest.first(size));
    }

    @Override
    public Slice<T> findAll(final Pageable pageable) {
        final CassandraPageRequest req = pageable instanceof CassandraPageRequest ? (CassandraPageRequest) pageable : CassandraPageRequest.first(2);
        final Query query = query().pageRequest(req);

        final Slice<T> slice =  cassandra.slice(query, clazz).map(e -> toModel(e));

        return slice;
    }

    @Override
    public boolean deleteById(final K id) {
        if (id == null) {
            return false;
        }
        return cassandra.deleteById(id, clazz);
    }

    @Override
    public void deleteAllByKeys(final Collection<K> ids) {
        if (ids.isEmpty()) {
            return;
        }

        // TODO find a batch delete solution
        ids.stream().forEach(id -> cassandra.deleteById(id, clazz));
//        cassandra.delete(query(where("id").in(ids)), clazz);
    }

    @Override
    public void deleteAll(final Collection<T> models) {
        if (models.isEmpty()) {
            return;
        }

        final Collection<K> keys = models.stream()
                .map(m -> toEntity(m).getId())
                .collect(toList());

        deleteAllByKeys(keys);
    }

    public boolean updateColumn(final K id,
                             final String columnName, final Object value) {
        return cassandra.update(query(where("id").is(id)),
                update(columnName, value), clazz);
    }

    public boolean updateColumns(final K id,
                              final String columnName1, final Object value1,
                              final String columnName2, final Object value2) {
        return cassandra.update(query(where("id").is(id)),
                update(columnName1, value1).set(columnName2, value2), clazz);
    }

    public boolean updateColumns(final K id,
                              final String columnName1, final Object value1,
                              final String columnName2, final Object value2,
                              final String columnName3, final Object value3) {
        return cassandra.update(query(where("id").is(id)),
                update(columnName1, value1).set(columnName2, value2).set(columnName3, value3), clazz);
    }

    public boolean updateColumns(final K id, final Map<String, Object> values) {
        Update update = Update.empty();
        for (final Map.Entry<String, Object> e : values.entrySet()) {
            update = update.set(e.getKey(), e.getValue());
        }
        return cassandra.update(query(where("id").is(id)), update, clazz);
    }

    public abstract T toModel(E entity);

    public abstract E toEntity(T model);
}
