package com.bwee.webit.search;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.SearchType;
import com.bwee.webit.search.model.AlbumDocument;
import com.bwee.webit.search.query.QueryStrategy;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Slf4j
@Service
public class AlbumEsService extends AbstractEsService<Album, AlbumDocument> {

    private final ElasticsearchOperations es;
    private final Map<SearchType, QueryStrategy> queryStrategies;

    @Autowired
    public AlbumEsService(final AlbumEsRepository repository,
                          final ElasticsearchOperations es,
                          @Qualifier("albumQueryStrategies")
                          final Map<SearchType, QueryStrategy> queryStrategies) {
        super(repository, AlbumDocument.class);
        this.es = es;
        this.queryStrategies = queryStrategies;
    }

    public List<String> search(final String keywords,
                               final SearchType searchType,
                               final Pageable pageable) {
        // Get query strategy for the search type
        final QueryStrategy queryStrategy = Optional.ofNullable(queryStrategies.get(searchType))
                .orElseThrow(() -> new IllegalStateException("No search strategy found for type: " + searchType));

        // Create query
        final Query query = queryStrategy.buildQuery(keywords, pageable);

        return es.search(query, AlbumDocument.class).stream()
                .sorted(comparing(hit -> - hit.getScore()))
                .map(m -> m.getId())
                .collect(Collectors.toList());
    }

    public List<String> searchRandom(final long seed, final Pageable pageable) {
        final QueryBuilder queryBuilder = QueryBuilders.functionScoreQuery(QueryBuilders.matchAllQuery(),
                ScoreFunctionBuilders.randomFunction().seed(seed));

        final Query query = new NativeSearchQuery(queryBuilder)
                .setPageable(pageable);

        return es.search(query, AlbumDocument.class).stream()
                .sorted(comparing(hit -> - hit.getScore()))
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
