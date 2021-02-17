package com.bwee.webit.search;

import com.bwee.webit.search.document.YoutubeDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface YoutubeEsRepository extends ElasticsearchRepository<YoutubeDocument, String> {

    @Query("{}")
    Page<YoutubeDocument> search(final String keywords, final Pageable pageable);
}
