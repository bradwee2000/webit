package com.bwee.webit.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CrudService<T> {

    void save(final T model);

    void saveAll(final Collection<T> model);

    void saveAll(final T model, final T ... more);

    Optional<T> findById(final String id);

    List<T> findByIds(final Collection<String> ids);

    List<T> findByIdsSorted(final List<String> ids);

    T findByIdStrict(final String id);

    void deleteById(final String id);

    void deleteAll(final Collection<String> ids);

    Slice<T> findAll(final Pageable pageable);
}
