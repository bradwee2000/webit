package com.bwee.webit.search.model;

public interface SearchDocument<T> {

    String getId();

    T toModel();
}
