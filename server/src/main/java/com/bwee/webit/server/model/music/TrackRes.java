package com.bwee.webit.server.model.music;

import com.bwee.webit.model.Track;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class TrackRes {
    private final String id;
    private final String title;
    private final String artist;
    private final String albumId;
    private final String albumName;
    private final String imageUrl;
    private final Long durationMillis;
    private final Integer trackNum;

    public TrackRes(final Track track) {
        this.id = track.getId();
        this.title = track.getTitle();
        this.artist = track.getArtist();
        this.albumId = track.getAlbumId();
        this.albumName = track.getAlbumName();
        this.imageUrl = track.getImageUrl();
        this.durationMillis = track.getDurationMillis();
        this.trackNum = StringUtils.isEmpty(track.getTrackNum()) ?
                null :
                Integer.parseInt(track.getTrackNum());
    }
}
