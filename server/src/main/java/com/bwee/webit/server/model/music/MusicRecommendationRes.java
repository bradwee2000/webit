package com.bwee.webit.server.model.music;

import com.bwee.webit.model.Album;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Data
@Accessors(chain = true)
public class MusicRecommendationRes {

    private List<AlbumRes> albumsToTry = emptyList();
    private List<AlbumRes> recentlyPlayed = emptyList();
    private List<AlbumRes> similarAlbums = emptyList();

    public MusicRecommendationRes setAlbumsToTry(final List<Album> albums) {
        this.albumsToTry = AlbumRes.of(albums);
        return this;
    }

    public MusicRecommendationRes setRecentlyPlayed(final List<Album> albums) {
        this.recentlyPlayed = AlbumRes.of(albums);
        return this;
    }

    public MusicRecommendationRes setSimilarAlbums(final List<Album> albums) {
        this.similarAlbums = AlbumRes.of(albums);
        return this;
    }
}
