package com.bwee.webit.service;

import com.bwee.webit.datasource.AlbumDbService;
import com.bwee.webit.exception.AlbumNotFoundException;
import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import com.bwee.webit.search.AlbumEsService;
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
public class AlbumRecommendationService {

    private final AlbumDbService albumDbService;
    private final AlbumEsService albumEsService;

    @Autowired
    public AlbumRecommendationService(final AlbumDbService albumDbService,
                                      final AlbumEsService albumEsService) {
        this.albumDbService = albumDbService;
        this.albumEsService = albumEsService;
    }


    public List<Album> findAlbumsToTry() {
        return findAlbumsToTry(PageRequest.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE));
    }

    public List<Album> findAlbumsToTry(final Pageable pageable) {
        final List<String> ids = albumEsService.searchRandom((long) (Math.random() * 1000) , pageable);
        return albumDbService.findByIds(ids).stream()
                .sorted(Ordering.explicit(ids).onResultOf(Album::getId))
                .collect(toList());
    }
}
