package com.bwee.webit.search.query;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

public class SimpleTrackQueryStrategy implements TrackQueryStrategy {

    private static final Map<String, Float> SEARCH_FIELDS = ImmutableMap.<String, Float>builder()
            .put("title", 1.0f)
            .put("albumName", 0.8f)
            .put("artist", 1.0f)
            .put("originalArtist", 1.0f)
            .put("composer", 1.0f)
            .put("genre", 1.0f)
            .put("tags", 1.0f)
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
