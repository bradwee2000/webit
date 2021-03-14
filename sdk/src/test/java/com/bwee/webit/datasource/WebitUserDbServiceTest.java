package com.bwee.webit.datasource;

import com.bwee.webit.datasource.CassandraConfiguration;
import com.bwee.webit.datasource.WebitUserDbService;
import com.bwee.webit.model.WebitUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@EmbeddedCassandra
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "spring.data.cassandra.keyspace-name=\"testWebitUser\"",
        "spring.data.cassandra.port=9142",
        "spring.data.cassandra.schema-action=RECREATE"
})
@ContextConfiguration(classes = {WebitUserDbService.class, CassandraConfiguration.class})
class WebitUserDbServiceTest {

    @Autowired
    private WebitUserDbService dbService;

    private WebitUser john, mary, steve;

    @BeforeAll
    @SneakyThrows
    public static void beforeAll() {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(10_000L);
        log.info("Using test cassandra host {}:{}",
                EmbeddedCassandraServerHelper.getHost(), EmbeddedCassandraServerHelper.getRpcPort());
    }

    @BeforeEach
    public void before() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
        john = new WebitUser().setId("123").setName("John").setPassword("XXX").setUsername("john01").setRoles(List.of("ADMIN", "SUPV"));
        mary = new WebitUser().setId("456").setName("Mary").setPassword("YYY").setUsername("mary91").setRoles(List.of("SUPV"));
        steve = new WebitUser().setId("789").setName("Steve").setPassword("ZZZ").setUsername("steve_1").setRoles(List.of("ADMIN"));

        dbService.deleteById(john.getId());
        dbService.deleteById(mary.getId());
    }

    @Test
    public void testSaveAndFindById_shouldReturnEqualObject() {
        dbService.save(john);
        dbService.save(mary);
        assertThat(dbService.findById("123")).hasValue(john);
        assertThat(dbService.findById("456")).hasValue(mary);
    }

    @Test
    public void testSaveAndFindByUsername_shouldReturnEqualObject() {
        dbService.save(john);
        dbService.save(mary);
        assertThat(dbService.findByUsername("john01")).hasValue(john);
        assertThat(dbService.findByUsername("mary91")).hasValue(mary);
    }

    @Test
    public void testSaveAll_shouldSaveAllRecords() {
        dbService.saveAll(List.of(steve, john));
        assertThat(dbService.findById("123")).hasValue(john);
        assertThat(dbService.findById("789")).hasValue(steve);
    }

    @Test
    public void testFindByUnknownUsername_shouldReturnEmpty() {
        assertThat(dbService.findByUsername("UNKNOWN")).isEmpty();
    }
}