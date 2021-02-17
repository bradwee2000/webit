package com.bwee.webit.server.model.music;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Data
@Accessors(chain = true)
public class SearchMusicAllResp {

    private List<AlbumResp> albums = emptyList();
    private List<MusicResp> tracks = emptyList();

    public SearchMusicAllResp setAlbums(List<Album> albums) {
        this.albums = albums.stream().map(AlbumResp::new).collect(toList());
        return this;
    }

    public SearchMusicAllResp setTracks(List<Track> tracks) {
        this.tracks = tracks.stream().map(MusicResp::new).collect(toList());
        return this;
    }

    @Data
    public static class AlbumResp {
        private final String id;
        private final String name;
        private final List<String> artists;
        private final int year;
        private final String imageUrl;
        private final List<String> tags;

        public AlbumResp(final Album album) {
            this.id = album.getId();
            this.name = album.getName();
            this.imageUrl = album.getImageUrl();
            this.artists = album.getArtists();
            this.year = album.getYear();
            this.tags = album.getTags();
        }
    }

    @Data
    public static class MusicResp {
        private final String id;
        private final String title;
        private final String artist;
        private final String imageUrl;
        private final Long durationMillis;

        public MusicResp(final Track track) {
            this.id = track.getId();
            this.title = track.getTitle();
            this.artist = track.getArtist();
            this.imageUrl = track.getImageUrl();
            this.durationMillis = track.getDurationMillis();
        }
    }
}
