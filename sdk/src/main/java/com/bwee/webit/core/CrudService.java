package com.bwee.webit.core;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Collection;
import java.util.Optional;

public interface CrudService<T> {

    void save(final T model);

    void saveAll(final Collection<T> model);

    void saveAll(final T model, final T ... more);

    Optional<T> findById(final String id);

    T findByIdStrict(final String id);

    void deleteById(final String id);

    Slice<T> findAll(final Pageable pageable);
}
