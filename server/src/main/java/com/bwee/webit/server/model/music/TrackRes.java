package com.bwee.webit.server.model.music;

import com.bwee.webit.model.Track;
import lombok.Data;

@Data
public class TrackRes {
    private final String id;
    private final String title;
    private final String artist;
    private final String albumName;
    private final String imageUrl;
    private final Long durationMillis;

    public TrackRes(final Track track) {
        this.id = track.getId();
        this.title = track.getTitle();
        this.artist = track.getArtist();
        this.albumName = track.getAlbumName();
        this.imageUrl = track.getImageUrl();
        this.durationMillis = track.getDurationMillis();
    }
}
