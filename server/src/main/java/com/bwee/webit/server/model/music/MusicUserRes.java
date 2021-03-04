package com.bwee.webit.server.model.music;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import static java.util.Collections.emptyList;

@Data
@Accessors(chain = true)
public class MusicUserRes {

    private List<Track> tracks = emptyList();

    private Integer currentTrackIndex;

    private Boolean isShuffle;

    private Boolean isLoop;

    private Boolean isPlaying;

    private Album selectedAlbum;

    private Track selectedTrack;
}
