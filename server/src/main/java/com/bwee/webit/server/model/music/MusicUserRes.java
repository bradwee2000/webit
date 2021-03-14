package com.bwee.webit.server.model.music;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import static java.util.Collections.emptyList;

@Data
@Accessors(chain = true)
public class MusicUserRes {

    private List<TrackRes> tracks = emptyList();

    private Integer currentTrackIndex;

    private Boolean isShuffle;

    private Boolean isLoop;

    private Boolean isPlaying;

    private AlbumRes selectedAlbum;

    private TrackRes selectedTrack;
}
