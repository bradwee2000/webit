package com.bwee.webit.server.model.music;

import com.bwee.webit.model.Album;
import lombok.Data;

import java.util.List;

@Data
public class AlbumRes {
    private final String id;
    private final String name;
    private final List<String> artists;
    private final int year;
    private final String imageUrl;
    private final List<String> tags;

    public AlbumRes(final Album album) {
        this.id = album.getId();
        this.name = album.getName();
        this.imageUrl = album.getImageUrl();
        this.artists = album.getArtists();
        this.year = album.getYear();
        this.tags = album.getTags();
    }
}
