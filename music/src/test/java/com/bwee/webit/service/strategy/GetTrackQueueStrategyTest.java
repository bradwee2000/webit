package com.bwee.webit.service.strategy;

import com.bwee.webit.model.MusicUser;
import com.bwee.webit.service.strategy.GetTrackQueueStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class GetTrackQueueStrategyTest {

    private final GetTrackQueueStrategy strategy = new GetTrackQueueStrategy();

    private MusicUser user;

    @BeforeEach
    public void before() {
        final List<String> trackIds = IntStream.range(0, 5).mapToObj(i -> String.valueOf(i)).collect(Collectors.toList());
        user = new MusicUser().setId("1").setTrackIdQueue(trackIds).setCurrentTrackIndex(0);
    }

    @Test
    public void testWithLoopDisabled_shouldReturnListAfterCurrentIndex() {
        user.setCurrentTrackIndex(0);
        assertThat(strategy.apply(user)).containsExactly("0", "1", "2", "3", "4");

        user.setCurrentTrackIndex(1);
        assertThat(strategy.apply(user)).containsExactly("1", "2", "3", "4");

        user.setCurrentTrackIndex(4);
        assertThat(strategy.apply(user)).containsExactly("4");
    }

    @Test
    public void testWithLoopEnabled_shouldReturnFullListWithCurrentIndexAtTop() {
        user.setLoop(true);
        user.setCurrentTrackIndex(0);
        assertThat(strategy.apply(user)).containsExactly("0", "1", "2", "3", "4");

        user.setCurrentTrackIndex(1);
        assertThat(strategy.apply(user)).containsExactly("1", "2", "3", "4", "0");

        user.setCurrentTrackIndex(4);
        assertThat(strategy.apply(user)).containsExactly("4", "0", "1", "2", "3");
    }

    @Test
    public void testEmptyList_shouldReturnEmptyList() {
        user.setLoop(true)
                .setCurrentTrackIndex(0)
                .setTrackIdQueue(Collections.emptyList());
        assertThat(strategy.apply(user)).isEmpty();
    }
}