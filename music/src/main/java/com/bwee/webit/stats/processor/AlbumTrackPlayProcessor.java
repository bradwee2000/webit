package com.bwee.webit.stats.processor;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import com.bwee.webit.stats.MusicStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlbumTrackPlayProcessor implements StatProcessor {

    @Autowired
    private MusicStatsService musicStatsService;

    @Override
    public void consume(final Object[] args) {

        final Album album = (Album) args[0];
        final Track track = (Track) args[1];

        if (album != null) {
            musicStatsService.recordAlbumPlay(album.getId());
        }

        if (track != null) {
            musicStatsService.recordTrackPlay(track.getId());
        }
    }
}
