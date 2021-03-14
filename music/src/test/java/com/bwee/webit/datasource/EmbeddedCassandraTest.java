package com.bwee.webit.datasource;

import com.bwee.webit.datasource.CassandraConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@EmbeddedCassandra
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "spring.data.cassandra.keyspace-name=\"testwebit\"",
        "spring.data.cassandra.port=9142",
        "spring.data.cassandra.schema-action=CREATE_IF_NOT_EXISTS"
})
@Import(CassandraConfiguration.class)
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
}
