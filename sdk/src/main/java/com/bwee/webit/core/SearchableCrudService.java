package com.bwee.webit.core;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchableCrudService<T> extends CrudService<T> {

    List<T> search(final String keywords);

    List<T> search(final String keywords, final Pageable pageable);
}
