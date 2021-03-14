package com.bwee.webit.server.model.music;

import com.bwee.webit.model.Track;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@Accessors(chain = true)
public class SearchTrackRes {

    private final List<TrackRes> tracks;

    public SearchTrackRes(final Collection<Track> tracks) {
        this.tracks = tracks.stream().map(TrackRes::new).collect(toList());
    }
}
