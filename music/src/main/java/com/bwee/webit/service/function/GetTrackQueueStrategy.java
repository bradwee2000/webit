package com.bwee.webit.service.function;

import com.bwee.webit.model.MusicUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
public class GetTrackQueueStrategy implements Function<MusicUser, List<String>> {

    @Override
    public List<String> apply(final MusicUser user) {
        final List<String> trackIds = new ArrayList<>(user.getTrackIdQueue());
        final int currentTrackNum = user.getCurrentTrackIndex();

        final List<String> subList = trackIds.subList(currentTrackNum, trackIds.size());

        if (user.isLoop() && currentTrackNum > 0) {
            subList.addAll(trackIds.subList(0, currentTrackNum));
        }
        return subList;
    }
}
