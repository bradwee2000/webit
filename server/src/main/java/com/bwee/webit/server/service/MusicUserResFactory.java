package com.bwee.webit.server.service;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.MusicUser;
import com.bwee.webit.model.Track;
import com.bwee.webit.server.model.music.MusicUserRes;
import com.bwee.webit.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicUserResFactory {

    @Autowired
    private TrackService trackService;

    public MusicUserRes build(final MusicUser user) {
        return build(user, null);
    }

    public MusicUserRes build(final MusicUser user, final Album album) {
        final List<Track> tracks = trackService.findByIdsSorted(user.getTrackIdQueue());
        final Track selectedTrack = tracks.isEmpty() ? null : tracks.get(user.getCurrentTrackIndex());
        return new MusicUserRes()
                .setCurrentTrackIndex(user.getCurrentTrackIndex())
                .setIsLoop(user.isLoop())
                .setIsPlaying(user.isPlaying())
                .setIsShuffle(user.isShuffle())
                .setTracks(tracks)
                .setSelectedTrack(selectedTrack)
                .setSelectedAlbum(album);
    }
}
