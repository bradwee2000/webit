package com.bwee.webit.search;

import com.bwee.webit.search.model.TrackDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TrackEsRepository extends ElasticsearchRepository<TrackDocument, String> {

    @Query("{}")
    Page<TrackDocument> search(final String keywords, final Pageable pageable);
}
