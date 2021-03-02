package com.bwee.webit.search;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;

@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackageClasses = { AbstractEsService.class })
public class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

    @Value("${spring.data.elasticsearch.cluster-nodes:localhost:9300}")
    private List<String> clusterNodes;

    @Value("${spring.data.elasticsearch.cluster-name:}")
    private String clusterName;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        log.info("Using Elasticsearch nodes: {}", clusterNodes);
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(clusterNodes.toArray(new String[]{}))
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate(RestHighLevelClient client) {
//        return new ElasticsearchRestTemplate(client);
//    }
}
