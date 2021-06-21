package com.bwee.webit.search.query;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Slf4j
public class SimpleAlbumQueryStrategy implements AlbumQueryStrategy {

    private static final Map<String, Float> SEARCH_FIELDS_STRICT = ImmutableMap.<String, Float>builder()
            .put("name", 1.5f)
            .put("artists", 1.0f)
            .put("trackNames", 1.0f)
            .put("tags", 1.2f)
            .put("year", 1f)
            .build();

    private static final Map<String, Float> SEARCH_FIELDS_FUZZY = ImmutableMap.<String, Float>builder()
            .put("name", 1.5f)
            .put("artists", 1.0f)
            .put("trackNames", 1.0f)
            .put("tags", 1.2f)
            .build();

    @Override
    public Query buildQuery(final String keywords, final Pageable pageable) {

        // Do a strict keyword search
        final MultiMatchQueryBuilder multiMatchQueryStrict = multiMatchQuery(keywords)
                .fields(SEARCH_FIELDS_STRICT)
                .lenient(true); // allow search in numeric fields without throwing error

        // Do a search with some fuzziness to allow misspellings.
        final MultiMatchQueryBuilder multiMatchQuerySecondary = multiMatchQuery(keywords)
                .fields(SEARCH_FIELDS_FUZZY)
                .fuzziness(Fuzziness.ONE)
                .lenient(true);

        final BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .should(multiMatchQueryStrict)
                .should(multiMatchQuerySecondary);

        final Query query = new NativeSearchQuery(boolQueryBuilder)
                .setPageable(pageable);
        return query;
    }
}
