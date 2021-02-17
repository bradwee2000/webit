package com.bwee.webit.service.function;

import com.bwee.webit.datasource.TrackDbService;
import com.bwee.webit.model.MusicUser;
import com.bwee.webit.model.Track;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Slf4j
@Component
public class TrackNumSortStrategy implements TrackSortStrategy {

    @Autowired
    private TrackDbService trackDbService;

    @Override
    public MusicUser apply(final MusicUser user) {
        if (user.getTrackIdQueue().size() < 2) {
            return MusicUser.copyOf(user).setCurrentTrackIndex(0);
        }

        final String currentTrackId = user.getTrackIdQueue().get(user.getCurrentTrackIndex());
        final List<Track> tracks = trackDbService.findByIdsSorted(user.getTrackIdQueue());

        final List<String> trackIds = tracks.stream()
                .sorted(comparing(Track::getTrack))
                .map(Track::getId)
                .collect(Collectors.toList());

        return MusicUser.copyOf(user).setTrackIdQueue(trackIds).setCurrentTrackIndex(trackIds.indexOf(currentTrackId));
    }
}
