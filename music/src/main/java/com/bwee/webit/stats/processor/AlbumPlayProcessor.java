package com.bwee.webit.stats.processor;

import com.bwee.webit.stats.MusicStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlbumPlayProcessor implements StatProcessor {

    @Autowired
    private MusicStatsService musicStatsService;

    @Override
    public void consume(final Object[] args) {
        final String albumId = (String) args[0];
        musicStatsService.recordAlbumPlay(albumId);
    }
}
