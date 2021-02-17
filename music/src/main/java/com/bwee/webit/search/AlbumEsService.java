package com.bwee.webit.search;

import com.bwee.webit.model.Album;
import com.bwee.webit.search.model.AlbumDocument;
import com.bwee.webit.search.query.AlbumQueryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumEsService extends AbstractEsService<Album, AlbumDocument> {

    private AlbumEsRepository repository;
    private ElasticsearchOperations es;
    private AlbumQueryStrategy queryStrategy;

    @Autowired
    public AlbumEsService(final AlbumEsRepository repository,
                          final ElasticsearchOperations es,
                          final AlbumQueryStrategy queryStrategy) {
        super(repository, AlbumDocument.class);
        this.repository = repository;
        this.es = es;
        this.queryStrategy = queryStrategy;
    }

    public List<String> search(final String keywords, final Pageable pageable) {
        final Query query = queryStrategy.buildQuery(keywords, pageable);

        return es.search(query, AlbumDocument.class).stream()
                .sorted(Comparator.comparing(hit -> - hit.getScore()))
                .map(m -> m.getId())
                .collect(Collectors.toList());
    }

    @Override
    public Album toModel(final AlbumDocument document) {
        return document.toModel();
    }

    @Override
    public AlbumDocument toDocument(final Album model) {
        return new AlbumDocument(model);
    }
}
