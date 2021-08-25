package com.bwee.webit.search;

import com.bwee.webit.model.SearchType;
import com.bwee.webit.search.model.AlbumDocument;
import com.bwee.webit.search.query.QueryStrategy;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsImpl;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(SpringExtension.class)
@Import(AlbumEsService.class)
@ContextConfiguration
class AlbumEsServiceTest {

    @Autowired
    private AlbumEsService albumEsService;

    @MockBean
    private ElasticsearchOperations es;

    @MockBean
    private AlbumEsRepository albumEsRepository;

    @Autowired
    private QueryStrategy albumQueryStrategy;

    @BeforeEach
    public void before() {
        when(albumQueryStrategy.buildQuery(anyString(), any(Pageable.class))).thenReturn(mock(Query.class));
    }

    @Test
    public void testSearch_shouldSortResultsByHitScoreDescending() {
        final List<SearchHit<AlbumDocument>> searchHitList = Arrays.asList(
                new SearchHit("album", "111", 1.1f, null, null, new AlbumDocument()),
                new SearchHit("album", "222", 1.3f, null, null, new AlbumDocument()),
                new SearchHit("album", "333", 3.0f, null, null, new AlbumDocument())
        );
        final SearchHits<AlbumDocument> searchHist = new SearchHitsImpl<>(3, null, 3, null, searchHitList, null);

        when(es.search(any(Query.class), any(Class.class))).thenReturn(searchHist);

        final List<String> ids = albumEsService.search("Test", SearchType.allFields, PageRequest.of(0, 3));

        assertThat(ids).containsExactly("333", "222", "111");
    }

    @Configuration
    public static class Ctx {

        @Bean
        public QueryStrategy albumQueryStrategy() {
            return mock(QueryStrategy.class);
        }

        @Bean
        public Map<SearchType, QueryStrategy> albumQueryStrategies() {
            return ImmutableMap.of(SearchType.allFields, albumQueryStrategy());
        }
    }
}