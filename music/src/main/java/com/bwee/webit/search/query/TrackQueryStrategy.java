package com.bwee.webit.search.query;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Slf4j
public class TrackQueryStrategy implements QueryStrategy {

    private static final Map<String, Float> SEARCH_FIELDS_STRICT = ImmutableMap.<String, Float>builder()
            .put("title", 1.0f)
            .put("albumName", 0.8f)
            .put("artists", 1.0f)
            .put("originalArtist", 1.0f)
            .put("composer", 1.0f)
            .put("genre", 1.0f)
            .put("tags", 1.0f)
            .put("year", 1.0f)
            .build();

    private static final Map<String, Float> SEARCH_FIELDS_FUZZY = ImmutableMap.<String, Float>builder()
            .put("title", 1.0f)
            .put("albumName", 0.8f)
            .put("artists", 1.0f)
            .put("originalArtist", 1.0f)
            .put("composer", 1.0f)
            .put("genre", 1.0f)
            .put("tags", 1.0f)
            .build();

    @Override
    public Query buildQuery(final String keywords, final Pageable pageable) {

        // Do a strict keyword search
        final MultiMatchQueryBuilder multiMatchQueryStrict = multiMatchQuery(keywords)
                .fields(SEARCH_FIELDS_STRICT)
                .operator(Operator.AND)
                .lenient(true); // allow search in numeric fields without throwing error

        // Do a search with some fuzziness to allow misspellings.
        final MultiMatchQueryBuilder multiMatchQueryFuzzy = multiMatchQuery(keywords)
                .fields(SEARCH_FIELDS_FUZZY)
                .fuzziness(Fuzziness.ONE)
                .prefixLength(2)
                .lenient(true);

        final BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .should(multiMatchQueryStrict)
                .should(multiMatchQueryFuzzy);

        return new NativeSearchQuery(boolQueryBuilder)
                .setPageable(pageable);
    }
}
