package com.bwee.webit.search.query;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Slf4j
public class ArtistQueryStrategy implements QueryStrategy {
    private static final String ARTIST_FIELD = "artists";
    private static final String WILDCARD = "*";

    @Override
    public Query buildQuery(final String keywords, final Pageable pageable) {

        final QueryBuilder strictQuery = matchQuery(ARTIST_FIELD, keywords);
        final QueryBuilder wildcard = wildcardQuery(ARTIST_FIELD, wildcard(keywords));
        final QueryBuilder fuzzy = matchQuery(ARTIST_FIELD, keywords).fuzziness(Fuzziness.ONE).boost(0.6f);

        final BoolQueryBuilder boolQuery = new BoolQueryBuilder()
                .should(strictQuery.boost(1.5f))
                .should(wildcard.boost(1f))
                .should(fuzzy.boost(0.8f));

        return new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withHighlightFields(new HighlightBuilder.Field(ARTIST_FIELD))
                .withPageable(pageable)
                .build();
    }

    private String wildcard(final String keywords) {
        return WILDCARD + Joiner.on(WILDCARD).join(Splitter.on(' ').omitEmptyStrings().split(keywords)) + WILDCARD;
    }
}
