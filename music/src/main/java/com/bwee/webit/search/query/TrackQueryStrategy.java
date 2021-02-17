package com.bwee.webit.search.query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.Query;

public interface TrackQueryStrategy {

    Query buildQuery(final String keywords, final Pageable pageable);
}
