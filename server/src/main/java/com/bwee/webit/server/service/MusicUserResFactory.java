package com.bwee.webit.server.service;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.MusicUser;
import com.bwee.webit.server.model.music.AlbumRes;
import com.bwee.webit.server.model.music.MusicUserRes;
import com.bwee.webit.server.model.music.TrackRes;
import com.bwee.webit.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MusicUserResFactory {

    @Autowired
    private TrackService trackService;

    public MusicUserRes build(final MusicUser user) {
        return doBuild(user, Optional.empty());
    }

    public MusicUserRes build(final MusicUser user, final Album album) {
        return doBuild(user, Optional.ofNullable(album));
    }

    public MusicUserRes doBuild(final MusicUser user, final Optional<Album> album) {
        final List<TrackRes> tracks = trackService.findByIdsSorted(user.getTrackIdQueue()).stream()
                .map(TrackRes::new)
                .collect(Collectors.toList());

        final TrackRes selectedTrack = tracks.isEmpty() ? null : tracks.get(user.getCurrentTrackIndex());

        return new MusicUserRes()
                .setCurrentTrackIndex(user.getCurrentTrackIndex())
                .setIsLoop(user.isLoop())
                .setIsPlaying(user.isPlaying())
                .setIsShuffle(user.isShuffle())
                .setTracks(tracks)
                .setSelectedTrack(selectedTrack)
                .setSelectedAlbum(album.map(AlbumRes::new).orElse(null));
    }
}
