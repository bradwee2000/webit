package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.Entity;
import com.google.common.collect.Ordering;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.cassandra.core.query.Criteria.where;
import static org.springframework.data.cassandra.core.query.Query.query;
import static org.springframework.data.cassandra.core.query.Update.update;

@Slf4j
public abstract class AbstractDbService<T, E extends Entity<E>> implements DbService<T, E> {

    private final CassandraOperations cassandra;
    private final Class<E> clazz;

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
    public T merge(final T model) {
        final E newEntity = toEntity(model);
        final E existing = cassandra.selectOne(Query.query(where("id").is(newEntity.getId())), clazz);

        final E mergedEntity;
        if (existing == null) {
            mergedEntity = newEntity;
        } else {
            mergedEntity = merge(existing, newEntity);
        }
        return toModel(cassandra.update(mergedEntity));
    }

    @Override
    public List<T> saveAll(final Collection<T> models) {
        if (models.isEmpty()) {
            return emptyList();
        }

        final List<E> entities = models.stream()
                .map(this::toEntity)
                .collect(toList());

        cassandra.batchOps().update(entities).execute();

        return entities.stream().map(this::toModel).collect(toList());
    }

    public List<T> mergeAll(final Collection<T> models) {
        if (models.isEmpty()) {
            return emptyList();
        }

        final List<E> newEntities = models.stream().map(this::toEntity).collect(toList());
        final List<String> ids = newEntities.stream().map(Entity::getId).collect(toList());

        final Map<String, E> existing = cassandra.select(query(where("id").in(ids)), clazz).stream()
                .collect(toMap(e -> e.getId(), e -> e));

        final List<E> merged = newEntities.stream()
                .map(newEntity -> {
                    if (existing.containsKey(newEntity.getId())) {
                        return merge(existing.get(newEntity.getId()), newEntity);
                    }
                    return newEntity;
                })
                .collect(toList());

        cassandra.batchOps().update(merged).execute();

        return merged.stream().map(e -> toModel(e)).collect(Collectors.toList());
    }

    protected E merge(final E existingEntity, final E newEntity) {
        return newEntity;
    }

    public Optional<T> findById(final String id) {
        if (StringUtils.isEmpty(id)) {
            return Optional.empty();
        }

        return Optional.ofNullable(cassandra.selectOne(Query.query(where("id").is(id)), clazz))
                .map(e -> toModel(e));
    }

    public List<T> findByIdsSorted(final List<String> ids) {
        if (ids.isEmpty()) {
            return emptyList();
        }
        return cassandra.select(query(where("id").in(ids)), clazz).stream()
                .sorted(Ordering.explicit(ids).onResultOf(E::getId))
                .map(entity -> toModel(entity))
                .collect(toList());
    }

    public List<T> findByIds(final Collection<String> ids) {
        if (ids.isEmpty()) {
            return emptyList();
        }
        return cassandra.select(query(where("id").in(ids)), clazz).stream()
                .map(entity -> toModel(entity))
                .collect(toList());
    }

    public List<T> findAll(final Pageable pageable) {
        final Query query = query().pageRequest(pageable);
        return cassandra.select(query, clazz).stream()
                .map(o -> toModel(o))
                .collect(toList());
    }

    public boolean deleteById(final String id) {
        if (StringUtils.isEmpty(id)) {
            return false;
        }

        return cassandra.delete(Query.query(where("id").is(id)), clazz);
    }

    public void updateColumn(final String id,
                             final String columnName, final Object value) {
        cassandra.update(query(where("id").is(id)),
                update(columnName, value), clazz);
    }

    public void updateColumns(final String id,
                              final String columnName1, final Object value1,
                              final String columnName2, final Object value2) {
        cassandra.update(query(where("id").is(id)),
                update(columnName1, value1).set(columnName2, value2), clazz);
    }

    public void updateColumns(final String id,
                              final String columnName1, final Object value1,
                              final String columnName2, final Object value2,
                              final String columnName3, final Object value3) {
        cassandra.update(query(where("id").is(id)),
                update(columnName1, value1).set(columnName2, value2).set(columnName3, value3), clazz);
    }

    public abstract T toModel(E entity);

    public abstract E toEntity(T model);
}
