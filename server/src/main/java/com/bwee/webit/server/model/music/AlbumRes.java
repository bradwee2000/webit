package com.bwee.webit.server.model.music;

import com.bwee.webit.model.Album;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AlbumRes {

    public static final List<AlbumRes> of(List<Album> albums) {
        if (albums == null) {
            return Collections.emptyList();
        }
        return albums.stream().map(AlbumRes::new).collect(Collectors.toList());
    }

    private final String id;
    private final String name;
    private final List<String> artists;
    private final Integer year;
    private final String imageUrl;
    private final List<String> tags;

    public AlbumRes(final Album album) {
        this.id = album.getId();
        this.name = album.getDisplayName();
        this.imageUrl = album.getImageUrl();
        this.artists = album.getArtists();
        this.year = album.getYear();
        this.tags = album.getTags();
    }
}
