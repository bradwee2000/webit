package com.bwee.webit.datasource;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@Slf4j
@EmbeddedCassandra
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "spring.data.cassandra.keyspace-name=\"testwebit\"",
        "spring.data.cassandra.port=9142",
        "spring.data.cassandra.schema-action=RECREATE"
})
@Import(CassandraConfiguration.class)
@ContextConfiguration
public class EmbeddedCassandraTest {

    @BeforeAll
    @SneakyThrows
    public static void beforeAll() {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(10_000L);
    }

    @BeforeEach
    public void cleanupBefore() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }

    @AfterAll
    public static void afterAll() {
        EmbeddedCassandraServerHelper.cleanDataEmbeddedCassandra("testwebit");
    }

    @Configuration
    public static class Ctx {

        @Bean
        public List<Converter> converters() {
            return Collections.emptyList();
        }
    }
}
