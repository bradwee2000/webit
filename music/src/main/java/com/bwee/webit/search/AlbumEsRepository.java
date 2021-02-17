package com.bwee.webit.search;

import com.bwee.webit.search.model.AlbumDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AlbumEsRepository extends ElasticsearchRepository<AlbumDocument, String> {
}
