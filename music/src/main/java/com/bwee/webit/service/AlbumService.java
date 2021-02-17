package com.bwee.webit.service;

import com.bwee.webit.datasource.AlbumDbService;
import com.bwee.webit.datasource.TrackDbService;
import com.bwee.webit.exception.AlbumContentNotFoundException;
import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import com.bwee.webit.search.AlbumEsService;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.bwee.webit.util.Constants.DEFAULT_PAGE_NUM;
import static com.bwee.webit.util.Constants.DEFAULT_PAGE_SIZE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
public class AlbumService implements SearchableCrudService<Album> {

    @Autowired
    private AlbumDbService albumDbService;

    @Autowired
    private AlbumEsService albumEsService;

    @Autowired
    private TrackDbService trackDbService;

    @Override
    public void save(final Album album) {
        final Album saved = albumDbService.save(album);
        albumEsService.save(saved);
    }

    @Override
    public void saveAll(final Collection<Album> album) {
        final List<Album> saved = albumDbService.saveAll(album);
        albumEsService.saveAll(saved);
    }

    public void merge(final Album album) {
        final Album merged = albumDbService.merge(album);
        albumEsService.save(merged);
    }

    public void mergeAll(final Collection<Album> albums) {
        final List<Album> saved = albumDbService.mergeAll(albums);
        albumEsService.saveAll(saved);
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
        return findById(id).orElseThrow(() -> new AlbumContentNotFoundException(id));
    }

    @Override
    public void deleteById(final String id) {
        albumDbService.deleteById(id);
        albumEsService.deleteById(id);
    }

    @Override
    public List<Album> findAll(final Pageable pageable) {
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
