package com.bwee.webit.search;

import com.bwee.webit.model.SearchType;
import com.bwee.webit.model.Track;
import com.bwee.webit.search.model.TrackDocument;
import com.bwee.webit.search.query.QueryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

@Service
public class TrackEsService extends AbstractEsService<Track, TrackDocument> {

    private ElasticsearchOperations es;
    private final Map<SearchType, QueryStrategy> queryStrategies;

    @Autowired
    public TrackEsService(final TrackEsRepository repository,
                          final ElasticsearchOperations es,
                          @Qualifier("trackQueryStrategies")
                          final Map<SearchType, QueryStrategy> queryStrategies) {
        super(repository, TrackDocument.class);
        this.es = es;
        this.queryStrategies = queryStrategies;
    }

    public List<SearchHit<TrackDocument>> search(final String keywords,
                                                 final SearchType searchType,
                                                 final Pageable pageable) {
        // Get query strategy for the search type
        final QueryStrategy queryStrategy = Optional.ofNullable(queryStrategies.get(searchType))
                .orElseThrow(() -> new IllegalStateException("No search strategy found for type: " + searchType));

        // Create query
        final Query query = queryStrategy.buildQuery(keywords, pageable);

        return es.search(query, TrackDocument.class).stream()
                .sorted(comparing(hit -> - hit.getScore()))
                .collect(toList());
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
