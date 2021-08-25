package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.Entity;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.cassandra.config.*;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DataCenterReplication;
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.datastax.oss.driver.api.core.config.DefaultDriverOption.*;

@Slf4j
@Configuration
@EnableCassandraRepositories(basePackageClasses = { Entity.class })
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name:default}")
    private String keyspaceName;

    @Value("${spring.data.cassandra.local-datacenter:datacenter1}")
    private String dataCenter;

    @Value("${spring.data.cassandra.contact-points:localhost}")
    private String contactPoints;

    @Value("${spring.data.cassandra.schema-action:NONE}")
    private SchemaAction schemaAction;

    @Value ("${spring.data.cassandra.port:9042}")
    private int port;

    @Value("${spring.data.cassandra.replication.factor:1}")
    private int replicationFactor;

    @Value("${spring.data.cassandra.request.timeout.millis:15000}")
    private long requestTimeoutMillis;

    @Autowired
    private List<Converter> converters;

    @Override
    protected String getKeyspaceName() {
        return keyspaceName;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] { Entity.class.getPackageName()};
    }

    @Override
    public SchemaAction getSchemaAction() {
        return schemaAction;
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification keyspace = CreateKeyspaceSpecification.createKeyspace(getKeyspaceName());
        DataCenterReplication dcr = DataCenterReplication.of(dataCenter, replicationFactor);
        keyspace.ifNotExists(true).withNetworkReplication(dcr);
        return Collections.singletonList(keyspace);
    }

    @Override
    public CqlSessionFactoryBean cassandraSession() {
        log.info("Using contactPoints={}, datacenter={}, keyspaceName={}, schemaAction:{}",
                contactPoints, dataCenter, keyspaceName, schemaAction);
        return super.cassandraSession();
    }

    @Override
    protected SessionBuilderConfigurer getSessionBuilderConfigurer() {
        return cqlSessionBuilder -> cqlSessionBuilder
                .withConfigLoader(DriverConfigLoader.programmaticBuilder()
                        .withDuration(REQUEST_TIMEOUT, Duration.ofMillis(requestTimeoutMillis))
                        .withDuration(CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofMillis(requestTimeoutMillis))
                        .withDuration(CONNECTION_CONNECT_TIMEOUT, Duration.ofMillis(requestTimeoutMillis))
                        .build());
    }

    @Override
    protected String getLocalDataCenter() {
        return dataCenter;
    }

    @Override
    public CassandraCustomConversions customConversions() {
        return new CassandraCustomConversions(converters);
    }
}
