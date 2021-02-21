package com.bwee.webit.service;

import com.bwee.webit.datasource.TrackDbService;
import com.bwee.webit.exception.TrackContentNotFoundException;
import com.bwee.webit.file.MusicFileService;
import com.bwee.webit.model.Track;
import com.bwee.webit.search.TrackEsService;
import com.bwee.webit.search.model.TrackDocument;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bwee.webit.util.Constants.DEFAULT_PAGE_NUM;
import static com.bwee.webit.util.Constants.DEFAULT_PAGE_SIZE;

@Slf4j
@Service
public class TrackService implements SearchableCrudService<Track> {

    @Autowired
    private TrackDbService db;

    @Autowired
    private TrackEsService es;

    @Autowired
    private MusicFileService musicFileService;

    @Autowired
    private ElasticsearchOperations esOps;

    @Override
    public void save(final Track track) {
        final Track saved = db.save(track);
        es.save(saved);
    }

    @Override
    public void saveAll(final Collection<Track> tracks) {
        final List<Track> saved = db.saveAll(tracks);
        es.saveAll(saved);
    }

    public void merge(final Track track) {
        final Track merged = db.merge(track);
        es.save(merged);
    }

    public void mergeAll(final Collection<Track> tracks) {
        final List<Track> saved = db.mergeAll(tracks);
        es.saveAll(saved);
    }

    @Override
    public void saveAll(final Track track, final Track... more) {
        saveAll(Lists.asList(track, more));
    }

    @Override
    public Optional<Track> findById(final String id) {
        return db.findById(id);
    }

    @Override
    public Track findByIdStrict(final String id) {
        return db.findById(id).orElseThrow(() -> new TrackContentNotFoundException(id));
    }

    @Override
    public void deleteById(final String id) {
        db.deleteById(id);
        es.deleteById(id);
    }

    @Override
    public Slice<Track> findAll(final Pageable pageable) {
        return db.findAll(pageable);
    }

    @Override
    public List<Track> search(String keywords) {
        return search(keywords, PageRequest.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE));
    }

    @Override
    public List<Track> search(final String keywords, final Pageable pageable) {
        final List<SearchHit<TrackDocument>> docs = es.search(keywords, pageable);

        final List<String> ids = docs.stream().map(d -> d.getId()).collect(Collectors.toList());
        return db.findByIds(ids).stream()
                .sorted(Ordering.explicit(ids).onResultOf(Track::getId))
                .collect(Collectors.toList());
    }

    public Path getSourcePathById(final String id) {
        return musicFileService.toStoragePath(findByIdStrict(id));
    }
}
