package com.bwee.webit.core;

public interface IdGenerator<T> {

    String generateId(T model);
}
