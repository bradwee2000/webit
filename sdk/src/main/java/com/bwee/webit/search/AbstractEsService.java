package com.bwee.webit.search;

import com.bwee.webit.search.model.SearchDocument;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.*;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public abstract class AbstractEsService<T, D extends SearchDocument<T>> {

    private final ElasticsearchRepository<D, String> es;
    private final Class<D> clazz;

    protected AbstractEsService(ElasticsearchRepository<D, String> es, Class<D> clazz) {
        this.es = es;
        this.clazz = clazz;
    }

    public boolean isExist(final String id) {
        return es.existsById(id);
    }

    public List<T> saveAll(final T model1, final T model2) {
        return saveAll(asList(model1, model2));
    }

    public List<T> saveAll(final T model1, final T model2, final T model3) {
        return saveAll(asList(model1, model2, model3));
    }

    public List<T> saveAll(final Collection<T> models) {
        final List<D> docs = models.stream().map(m -> toDocument(m)).collect(toList());
        return StreamSupport.stream(es.saveAll(docs).spliterator(), false)
                .map(d -> toModel(d))
                .collect(toList());
    }

    public T save(final T model) {
        return toModel(es.save(toDocument(model)));
    }

    public void delete(final T model) {
        es.delete(toDocument(model));
    }

    public void deleteById(final String id) {
        es.deleteById(id);
    }

    public void deleteAll(final Collection<String> ids) {
        if (ids.isEmpty()) {
            return;
        }
        es.deleteAll(es.findAllById(ids));
    }

    public Optional<T> findById(final String id) {
        return es.findById(id).map(m -> toModel(m));
    }

    public Page<T> findAll(final Pageable pageable) {
        return es.findAll(pageable).map(d -> toModel(d));
    }

    public abstract T toModel(D document);

    public abstract D toDocument(T model);
}
