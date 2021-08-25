package com.bwee.webit.server.service;

import com.bwee.webit.model.Album;
import com.bwee.webit.server.model.music.AlbumTracksRes;
import com.bwee.webit.server.model.music.TrackRes;
import com.bwee.webit.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumTrackResFactory {

    @Autowired
    private TrackService trackService;

    public AlbumTracksRes build(final Album album) {
        final List<TrackRes> tracks = trackService.findByAlbumId(album.getId()).stream()
                .map(track -> new TrackRes(track))
                .collect(Collectors.toList());

        return new AlbumTracksRes()
                .setId(album.getId())
                .setArtists(album.getArtists())
                .setName(album.getDisplayName())
                .setImageUrl(album.getImageUrl())
                .setYear(album.getYear()).setTags(album.getTags())
                .setTracks(tracks);
    }
}
