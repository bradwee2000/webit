package com.bwee.webit.service;

import com.bwee.webit.datasource.AlbumDbService;
import com.bwee.webit.exception.AlbumNotFoundException;
import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import com.bwee.webit.search.AlbumEsService;
import com.bwee.webit.search.ArtistEsService;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import lombok.extern.slf4j.Slf4j;
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
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class ArtistService {

    private final ArtistEsService artistEsService;

    @Autowired
    public ArtistService(final ArtistEsService artistEsService) {
        this.artistEsService = artistEsService;
    }

    public List<String> search(String keywords) {
        return search(keywords, PageRequest.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE));
    }

    public List<String> search(final String keywords, final Pageable pageable) {
        return artistEsService.search(keywords, pageable);
    }
}
