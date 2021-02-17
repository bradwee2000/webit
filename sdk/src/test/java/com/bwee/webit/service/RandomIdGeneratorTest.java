package com.bwee.webit.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RandomIdGenerator.class)
class RandomIdGeneratorTest {

    @Autowired
    private RandomIdGenerator generator;

    @Test
    public void testGenerateId_shouldReturnId() {
        assertThat(generator.generateId(null)).isNotEmpty().hasSizeGreaterThan(8);
    }

    @Test
    public void testGenerateMultipleIds_shouldReturnUniqueIds() {
        final int size = 10_000;
        final Set<String> ids = IntStream.range(0, size)
                .mapToObj(i -> generator.generateId(null))
                .collect(Collectors.toSet());
        assertThat(ids).hasSize(size);
    }
}