package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.Entity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.cassandra.core.query.Criteria.where;
import static org.springframework.data.cassandra.core.query.Query.query;

public interface DbService<T, E extends Entity<E>> {

    T save(final T model);

    List<T> saveAll(final Collection<T> models);

    T merge(final T model);

    Optional<T> findById(final String id);

    List<T> findByIds(final Collection<String> ids);

    List<T> findByIdsSorted(final List<String> ids);

    Slice<T> findAll();

    Slice<T> findAll(final int size);

    Slice<T> findAll(final Pageable pageable);

    boolean deleteById(final String id);

    void deleteAll(final Collection<String> ids);
}
