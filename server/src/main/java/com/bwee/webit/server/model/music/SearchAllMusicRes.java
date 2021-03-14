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
public class SearchAllMusicRes {

    private List<AlbumRes> albums = emptyList();
    private List<TrackRes> tracks = emptyList();

    public SearchAllMusicRes setAlbums(final List<Album> albums) {
        this.albums = albums.stream().map(AlbumRes::new).collect(toList());
        return this;
    }

    public SearchAllMusicRes setTracks(final List<Track> tracks) {
        this.tracks = tracks.stream().map(TrackRes::new).collect(toList());
        return this;
    }
}
