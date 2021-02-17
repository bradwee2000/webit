package com.bwee.webit.search;

import com.bwee.webit.model.Track;
import com.bwee.webit.search.model.TrackDocument;
import com.bwee.webit.search.query.TrackQueryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
public class TrackEsService extends AbstractEsService<Track, TrackDocument> {

    private TrackEsRepository repository;
    private ElasticsearchOperations es;
    private TrackQueryStrategy queryStrategy;

    @Autowired
    public TrackEsService(final TrackEsRepository repository,
                          final ElasticsearchOperations es,
                          final TrackQueryStrategy queryStrategy) {
        super(repository, TrackDocument.class);
        this.repository = repository;
        this.es = es;
        this.queryStrategy = queryStrategy;
    }

    public List<SearchHit<TrackDocument>> search(final String keywords, final Pageable pageable) {
        final Query query = queryStrategy.buildQuery(keywords, pageable);

        return es.search(query, TrackDocument.class).stream()
                .sorted(comparing(hit -> - hit.getScore()))
                .collect(Collectors.toList());
    }

    @Override
    public Track toModel(final TrackDocument document) {
        return document.toModel();
    }

    @Override
    public TrackDocument toDocument(final Track model) {
        return new TrackDocument(model);
    }
}
