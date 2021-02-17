package com.bwee.webit.service;

import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CrudService<T> {

    void save(final T model);

    void saveAll(final Collection<T> model);

    void saveAll(final T model, final T ... more);

    Optional<T> findById(final String id);

    T findByIdStrict(final String id);

    void deleteById(final String id);

    List<T> findAll(final Pageable pageable);
}
