package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.PersonEntity;
import com.bwee.webit.model.Person;
import com.datastax.oss.driver.api.core.DriverException;
import com.datastax.oss.driver.api.core.cql.Row;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.cql.RowMapper;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@EmbeddedCassandra
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "spring.data.cassandra.keyspace-name=\"testwebit\"",
        "spring.data.cassandra.port=9142",
        "spring.data.cassandra.schema-action=RECREATE_DROP_UNUSED"
})
@ContextConfiguration(classes = {AbstractDbServiceTest.Ctx.class, CassandraConfiguration.class})
class AbstractDbServiceTest {

    @Autowired
    private AbstractDbService<Person, PersonEntity, String> dbService;

    @Autowired
    private CassandraOperations cassandra;

    private Person john, mary, jane, beck;

    private final LocalDateTime now = LocalDateTime.of(2021, 1, 1, 12, 30, 40);

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
        john = new Person().setId("123").setName("John Doe").setTags(List.of("Bookworm", "Military")).setGender(Person.Gender.Male).setCreateTime(now);
        mary = new Person().setId("456").setName("Mary Jay").setTags(List.of("CEO", "GoForIt")).setCreateTime(now);
        jane = new Person().setId("789").setName("Jane Tan").setCreateTime(now);
        beck = new Person().setId("034").setName("Beck Lee").setCreateTime(now);

        dbService.deleteById(john.getId());
        dbService.deleteById(mary.getId());
        dbService.deleteById(jane.getId());
        dbService.deleteById(beck.getId());
    }

    @Test
    public void testSave_shouldSaveAndReturnResult() {
        final Person saved = dbService.save(john);
        assertThat(saved).isEqualTo(john);
    }

    @Test
    public void testFind_shouldReturnSavedPerson() {
        dbService.save(jane);
        dbService.save(john);

        cassandra.getCqlOperations().query("select * from person", new RowMapper<>() {
            @Override
            public String mapRow(Row row, int rowNum) throws DriverException {
                log.info("ROW: {}", row.getFormattedContents());
                return row.get("gender", String.class);
            }
        });

        assertThat(dbService.findById("123")).hasValue(john);
        assertThat(dbService.findById("123").get().getGender()).isEqualTo(Person.Gender.Male);
        assertThat(dbService.findById("789")).hasValue(jane);
    }

    @Test
    public void testFindWithUnknownId_shouldReturnEmpty() {
        dbService.save(john);
        assertThat(dbService.findById("UNKNOWN")).isEmpty();
    }

    @Test
    public void testUpdate_shouldUpdatePerson() {
        dbService.save(john);
        dbService.save(john.setName("John Doe III"));
        assertThat(dbService.findById(john.getId())).hasValueSatisfying(p -> "John Doe III".equals(p.getName()));
    }

    @Test
    public void testDelete_shouldDeleteFromDb() {
        dbService.save(jane);
        dbService.save(john);

        dbService.deleteById(john.getId());

        assertThat(dbService.findById(john.getId())).isEmpty();
    }

    @Test
    public void testDeleteAllByIds_shouldDeleteAllListedIdsFromDb() {
        dbService.saveAll(asList(jane, john, mary, beck));
        dbService.deleteAll(john, mary);
        assertThat(dbService.findAll().getContent()).containsExactlyInAnyOrder(jane, beck);
    }

    @Test
    public void testFindByIds_shouldReturnMultipleResultsMatchingIds() {
        dbService.save(jane);
        dbService.save(john);
        dbService.save(mary);

        assertThat(dbService.findByIds(List.of("123", "456", "789"))).containsExactlyInAnyOrder(jane, john, mary);
        assertThat(dbService.findByIds(List.of("456", "789"))).containsExactlyInAnyOrder(jane, mary);
        assertThat(dbService.findByIds(List.of("456"))).containsExactlyInAnyOrder(mary);
    }

    @Test
    public void testSaveAll_shouldSaveAllRecords() {
        final List<Person> persons = dbService.saveAll(List.of(jane, beck, mary));
        assertThat(persons).containsExactlyInAnyOrder(jane, beck, mary);
        assertThat(dbService.findAll().getContent()).containsExactlyInAnyOrder(jane, beck, mary);
    }

    @Test
    public void testFindAllByPage_shouldReturnResultsInPages() {
        final List<Person> people = List.of(john, jane, beck);
        dbService.saveAll(people);
        final List<Person> foundPeople = new ArrayList(3);

        final Slice<Person> found1 = dbService.findAll(1);
        assertThat(found1).hasSize(1).containsAnyElementsOf(people);
        assertThat(found1.hasNext()).isTrue();
        foundPeople.addAll(found1.getContent());

        final Slice<Person> found2 = dbService.findAll(found1.nextPageable());
        assertThat(found2).hasSize(1).containsAnyElementsOf(people);
        assertThat(found2.hasNext()).isTrue();
        foundPeople.addAll(found2.getContent());

        final Slice<Person> found3 = dbService.findAll(found2.nextPageable());
        assertThat(found3).hasSize(1).containsAnyElementsOf(people);
        assertThat(found3.hasNext()).isTrue();
        foundPeople.addAll(found3.getContent());

        assertThat(foundPeople).containsExactlyInAnyOrderElementsOf(people);
    }

    @Configuration
    public static class Ctx {
        @Bean
        public AbstractDbService<Person, PersonEntity, String> dbService(final CassandraOperations cassandraOperations) {
            return new AbstractDbService<>(cassandraOperations, PersonEntity.class) {

                @Override
                public Person toModel(PersonEntity entity) {
                    return entity.toModel();
                }

                @Override
                public PersonEntity toEntity(Person model) {
                    return new PersonEntity(model);
                }
            };
        }

        @Bean
        public Converter<Person.Gender, String> genderToStringConverter() {
            return new GenderToStringConverter();
        }

        @Bean
        public Converter<String, Person.Gender> stringToGenderConverter() {
            return new StringToGenderConverter();
        }
    }


    @WritingConverter
    public static class GenderToStringConverter implements Converter<Person.Gender, String> {
        @Override
        public String convert(Person.Gender gender) {
            return gender.getCode();
        }
    }

    @ReadingConverter
    public static class StringToGenderConverter implements Converter<String, Person.Gender> {
        @Override
        public Person.Gender convert(String code) {
            switch (code) {
                case "M": return Person.Gender.Male;
                case "F": return Person.Gender.Female;
            }
            return null;
        }
    }
}