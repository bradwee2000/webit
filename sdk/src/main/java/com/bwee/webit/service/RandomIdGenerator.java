package com.bwee.webit.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.stereotype.Service;

@Primary
@Service
public class RandomIdGenerator implements IdGenerator {

    private static final int DEFAULT_CHARACTER_SIZE = 12;

    private final int characterSize;

    public RandomIdGenerator() {
        this(DEFAULT_CHARACTER_SIZE);
    }

    public RandomIdGenerator(int characterSize) {
        this.characterSize = characterSize;
    }

    @Override
    public String generateId(Object model) {
        return RandomStringUtils.randomAlphanumeric(characterSize);
    }
}
