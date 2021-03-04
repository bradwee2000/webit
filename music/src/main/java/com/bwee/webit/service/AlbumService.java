package com.bwee.webit.service;

import com.bwee.webit.core.SearchableCrudService;
import com.bwee.webit.core.SimpleCrudService;
import com.bwee.webit.datasource.AlbumDbService;
import com.bwee.webit.datasource.DbService;
import com.bwee.webit.datasource.TrackDbService;
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

import java.util.*;

import static com.bwee.webit.util.Constants.DEFAULT_PAGE_NUM;
import static com.bwee.webit.util.Constants.DEFAULT_PAGE_SIZE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
public class AlbumService extends SimpleCrudService<Album> implements SearchableCrudService<Album> {

    private final AlbumDbService albumDbService;
    private final AlbumEsService albumEsService;
    private final TrackDbService trackDbService;

    @Autowired
    public AlbumService(final AlbumDbService albumDbService,
                        final AlbumEsService albumEsService,
                        final TrackDbService trackDbService) {
        super(albumDbService);
        this.albumDbService = albumDbService;
        this.albumEsService = albumEsService;
        this.trackDbService = trackDbService;
    }

    @Override
    public void save(final Album album) {
        albumDbService.save(album);
        albumEsService.save(album);
    }

    @Override
    public void saveAll(final Collection<Album> albums) {
        albumDbService.saveAll(albums);
        albumEsService.saveAll(albums);
    }

    @Override
    public void saveAll(final Album album, final Album ... more) {
        saveAll(Lists.asList(album, more));
    }

    public List<Album> findByIds(final Collection<String> ids) {
        return albumDbService.findByIds(ids);
    }

    @Override
    public Optional<Album> findById(final String id) {
        return albumDbService.findById(id).map(album -> {
            final Map<String, Integer> musicIdTrackMap = album.getTracks().stream()
                    .collect(toMap(music -> music.getId(), music -> music.getTrack()));

            // Populate music list and preserve the track numbers
            final List<Track> trackList = trackDbService.findByIds(musicIdTrackMap.keySet()).stream()
                    .map(music -> music.setTrack(musicIdTrackMap.get(music.getId())))
                    .sorted(comparing(Track::getTrack))
                    .collect(toList());

            return album.setTracks(trackList);
        });
    }

    @Override
    public Album findByIdStrict(final String id) {
        return findById(id).orElseThrow(() -> new AlbumNotFoundException(id));
    }

    @Override
    public void deleteById(final String id) {
        albumDbService.deleteById(id);
        albumEsService.deleteById(id);
    }

    @Override
    public Slice<Album> findAll(final Pageable pageable) {
        return albumDbService.findAll(pageable);
    }

    @Override
    public List<Album> search(String keywords) {
        return search(keywords, PageRequest.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE));
    }

    @Override
    public List<Album> search(final String keywords, final Pageable pageable) {
        final List<String> ids = albumEsService.search(keywords, pageable);

        return albumDbService.findByIds(ids).stream()
                .sorted(Ordering.explicit(ids).onResultOf(Album::getId))
                .collect(toList());
    }
}
