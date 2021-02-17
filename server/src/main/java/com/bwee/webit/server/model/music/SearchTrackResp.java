package com.bwee.webit.server.model.music;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Data
@Accessors(chain = true)
public class SearchTrackResp {

    private final List<TrackResp> tracks;

    public SearchTrackResp(final Collection<Track> tracks) {
        this.tracks = tracks.stream().map(TrackResp::new).collect(toList());
    }

    @Data
    public static class TrackResp {
        private final String id;
        private final String title;
        private final String artist;
        private final AlbumResp album;
        private final String imageUrl;
        private final Long durationMillis;

        public TrackResp(final Track track) {
            this.id = track.getId();
            this.title = track.getTitle();
            this.artist = track.getArtist();
            this.album = new AlbumResp(track.getAlbumId(), track.getAlbumName());
            this.imageUrl = track.getImageUrl();
            this.durationMillis = track.getDurationMillis();
        }
    }

    @Data
    public static class AlbumResp {
        private final String id;
        private final String name;

    }
}
