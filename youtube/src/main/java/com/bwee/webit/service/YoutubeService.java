package com.bwee.webit.service;

import com.bwee.webit.datasource.YoutubeDbService;
import com.bwee.webit.exception.YoutubeContentNotFoundException;
import com.bwee.webit.model.YoutubeVideo;
import com.bwee.webit.search.YoutubeEsService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.bwee.webit.util.Constants.DEFAULT_PAGE_NUM;
import static com.bwee.webit.util.Constants.DEFAULT_PAGE_SIZE;

@Service
public class YoutubeService implements SearchableCrudService<YoutubeVideo> {

    @Autowired
    private YoutubeDbService db;

    @Autowired
    private YoutubeEsService es;

    @Override
    public void save(YoutubeVideo video) {
        db.save(video);
        es.save(video);
    }

    @Override
    public void saveAll(final Collection<YoutubeVideo> videos) {
        db.saveAll(videos);
        es.saveAll(videos);
    }

    @Override
    public void saveAll(final YoutubeVideo video, final YoutubeVideo ... more) {
        saveAll(Lists.asList(video, more));
    }

    @Override
    public Optional<YoutubeVideo> findById(final String id) {
        return db.findById(id);
    }

    @Override
    public YoutubeVideo findByIdStrict(final String id) {
        return db.findById(id).orElseThrow(() -> new YoutubeContentNotFoundException(id));
    }

    @Override
    public void deleteById(final String id) {
        db.deleteById(id);
        es.deleteById(id);
    }

    @Override
    public Slice<YoutubeVideo> findAll(final Pageable pageable) {
        return db.findAll(pageable);
    }

    @Override
    public List<YoutubeVideo> search(String keywords) {
        return search(keywords, PageRequest.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE));
    }

    @Override
    public List<YoutubeVideo> search(final String keywords, final Pageable pageable) {
        return es.search(keywords, pageable);
    }
}
