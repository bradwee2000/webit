package com.bwee.webit.config;

import com.bwee.webit.datasource.entity.Entity;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.tinkerpop.shaded.kryo.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.SessionBuilderConfigurer;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DataCenterReplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

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
        CreateKeyspaceSpecification keyspace = CreateKeyspaceSpecification.createKeyspace(keyspaceName);
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
                        .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(requestTimeoutMillis))
                        .build());
    }

    @Override
    protected String getLocalDataCenter() {
        return dataCenter;
    }

    //    @Bean
//    public CqlSessionFactoryBean cqlSessionFactoryBean() {
//        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
//        session.setContactPoints("localhost");
//        session.setKeyspaceName("webit");
//        return session;
//    }

//    @Bean
//    public SessionFactoryFactoryBean sessionFactory(CqlSession session, CassandraConverter converter) {
//        final SessionFactoryFactoryBean sessionFactory = new SessionFactoryFactoryBean();
//        sessionFactory.setSession(session);
//        sessionFactory.setConverter(converter);
//        sessionFactory.setSchemaAction(SchemaAction.CREATE_IF_NOT_EXISTS);
//        return sessionFactory;
//    }

//    @Bean
//    public CassandraMappingContext mappingContext(CqlSession cqlSession) {
//        final CassandraMappingContext mappingContext = new CassandraMappingContext();
//        return mappingContext;
//    }

//    @Bean
//    public CassandraConverter converter(CqlSession cqlSession, CassandraMappingContext mappingContext) {
//        MappingCassandraConverter converter = new MappingCassandraConverter(mappingContext);
//        converter.setUserTypeResolver(new SimpleUserTypeResolver(cqlSession));
//        return converter;
//    }
//
//    @Bean
//    public CassandraOperations cassandraTemplate(SessionFactory sessionFactory, CassandraConverter converter) {
//        return new CassandraTemplate(sessionFactory, converter);
//    }
}
