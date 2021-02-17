package com.bwee.webit.search.query;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

public class SimpleAlbumQueryStrategy implements AlbumQueryStrategy {

    private static final Map<String, Float> SEARCH_FIELDS = ImmutableMap.<String, Float>builder()
            .put("name", 1.5f)
            .put("artists", 1.0f)
            .put("trackNames", 1.0f)
            .put("tags", 1.2f)
            .put("year", 1.0f)
            .build();

    @Override
    public Query buildQuery(final String keywords, final Pageable pageable) {
        final Query query = new NativeSearchQuery(multiMatchQuery(keywords)
                .fields(SEARCH_FIELDS)
                .fuzziness(Fuzziness.ONE))
                .setPageable(pageable);

        return query;
    }
}
