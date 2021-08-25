package com.bwee.webit.service;

import com.bwee.webit.exception.AlbumNotFoundException;
import com.bwee.webit.model.Album;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.concurrent.TimeUnit.MINUTES;

@Slf4j
@Component
public class AlbumImportCacheService {

    @Autowired
    private AlbumService albumService;

    private final LoadingCache<String, Album> albumImportLoadingCache;

    public AlbumImportCacheService() {
        albumImportLoadingCache =  CacheBuilder.newBuilder()
                .expireAfterWrite(5, MINUTES)
                .build(CacheLoader.from((albumId) -> albumService.findByIdStrict(albumId)));
    }

    public Optional<Album> get(final String albumId) {
        try {
            return Optional.ofNullable(albumImportLoadingCache.get(albumId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void put(final Album album) {
        albumService.save(album);
        albumImportLoadingCache.put(album.getId(), album);
    }
}
