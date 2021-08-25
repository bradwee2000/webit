package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.Entity;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.cassandra.core.query.Criteria.where;
import static org.springframework.data.cassandra.core.query.Query.query;

public interface DbService<T, E extends Entity<E, K>, K> {

    T save(final T model);

    default List<T> saveAll(final T model, final T ... more) {
        return saveAll(Lists.asList(model, more));
    }

    List<T> saveAll(final Collection<T> models);

    Optional<T> findById(final K id);

    List<T> findByIds(final Collection<K> ids);

    List<T> findByIdsSorted(final List<K> ids);

    Slice<T> findAll();

    Slice<T> findAll(final int size);

    Slice<T> findAll(final Pageable pageable);

    boolean deleteById(final K id);

    default void deleteAllByKeys(final K id, final K ... more) {
        deleteAllByKeys(Lists.asList(id, more));
    }

    void deleteAllByKeys(final Collection<K> ids);

    default void deleteAll(final T model, final T ... more) {
        deleteAll(Lists.asList(model, more));
    }

    void deleteAll(final Collection<T> models);
}
