package com.bwee.webit.search;

import com.bwee.webit.search.model.AlbumDocument;
import com.bwee.webit.search.query.QueryStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.replace;

@Slf4j
@Service
public class ArtistEsService {

    private final ElasticsearchOperations es;
    private final QueryStrategy queryStrategy;

    @Autowired
    public ArtistEsService(final ElasticsearchOperations es,
                           @Qualifier("artistQueryStrategy") final QueryStrategy queryStrategy) {
        this.es = es;
        this.queryStrategy = queryStrategy;
    }

    public List<String> search(final String keywords, final Pageable pageable) {
        final Query query = queryStrategy.buildQuery(keywords, pageable);

        return es.search(query, AlbumDocument.class).stream()
                .sorted(comparing(SearchHit::getScore, reverseOrder()))
                .peek(m -> log.info("DEEZNUTS {} {}", m.getScore(), m.getHighlightFields()))
                .flatMap(m -> m.getHighlightFields().values().stream())
                .flatMap(artists -> artists.stream())
                .filter(artist -> artist.contains("<em>"))
                .map(artist -> replace(artist, "<em>", ""))
                .map(artist -> replace(artist, "</em>", ""))
                .distinct()
                .collect(toList());
    }
}
