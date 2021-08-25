package com.bwee.webit.service.strategy.sort;

import com.bwee.webit.model.MusicUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class ShuffleSortStrategy implements TrackSortStrategy {

    private Random random = new Random();

    @Override
    public MusicUser apply(final MusicUser user) {
        if (user.getTrackIdQueue().size() < 2) {
            return MusicUser.copyOf(user).setCurrentTrackIndex(0);
        }

        final List<String> trackIds = new ArrayList(user.getTrackIdQueue());
        final int currentTrackNum = user.getCurrentTrackIndex();
        final String currentTrackId = trackIds.get(currentTrackNum);

        Collections.shuffle(trackIds, random);

        // push current track to top
        trackIds.remove(currentTrackId);
        trackIds.add(0, currentTrackId);

        return MusicUser.copyOf(user).setCurrentTrackIndex(0).setTrackIdQueue(trackIds);
    }
}
