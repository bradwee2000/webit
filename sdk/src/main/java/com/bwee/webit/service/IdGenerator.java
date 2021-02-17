package com.bwee.webit.service;

public interface IdGenerator<T> {

    String generateId(T model);
}
