package com.bwee.webit.service;

import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SearchableCrudService<T> extends CrudService<T> {

    List<T> search(final String keywords);

    List<T> search(final String keywords, final Pageable pageable);
}
