package com.bwee.webit.datasource.entity;

public interface Entity<E, K> {

    K getId();

    E setId(K key);
}
