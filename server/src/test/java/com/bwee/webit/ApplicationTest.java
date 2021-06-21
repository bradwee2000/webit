package com.bwee.webit;


import com.datastax.oss.driver.api.core.CqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SessionFactoryFactoryBean;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

@SpringBootTest
public class ApplicationTest {

    @MockBean
    private CassandraAdminTemplate cassandraTemplate;

    @MockBean
    private CassandraConverter cassandraConverter;

    @MockBean
    private CassandraMappingContext cassandraMapping;

    @MockBean
    private SessionFactoryFactoryBean cassandraSessionFactory;

    @MockBean
    private CqlTemplate cqlTemplate;

    @MockBean
    private CqlSession cqlSession;

    @Test
    public void testRun_shouldNotThrowErrors() {
        // nothing
    }
}