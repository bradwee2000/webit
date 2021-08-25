package com.bwee.webit.service.strategy.sort;

import com.bwee.webit.model.MusicUser;
import com.bwee.webit.service.strategy.sort.ShuffleSortStrategy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ShuffleSortStrategy.class)
class ShuffleSortStrategyTest {

    @Autowired
    private ShuffleSortStrategy strategy;

    private List<String> trackIds;
    private MusicUser user;

    @BeforeEach
    public void before() {
        user = new MusicUser().setCurrentTrackIndex(0);
    }

    @Test
    public void testApply_shouldShuffleTrackList() {
        trackIds = IntStream.range(0, 100).mapToObj(i -> "" + i).collect(Collectors.toList());
        user.setTrackIdQueue(trackIds);

        final MusicUser result = strategy.apply(user);

        assertThat(result.getTrackIdQueue()).hasSize(100).containsAll(trackIds).isNotEqualTo(trackIds);
        assertThat(result.getCurrentTrackIndex()).isEqualTo(0);
        assertThat(result.getTrackIdQueue().get(0)).isEqualTo("0");
    }

    @Test
    public void testApplyWithCurrentTrack_shouldPutCurrentTrackToTopOfList() {
        trackIds = IntStream.range(0, 10).mapToObj(i -> "" + i).collect(Collectors.toList());
        user.setTrackIdQueue(trackIds).setCurrentTrackIndex(9);

        final MusicUser result = strategy.apply(user);

        assertThat(result.getCurrentTrackIndex()).isEqualTo(0);
        assertThat(result.getTrackIdQueue().get(0)).isEqualTo("9");
    }

    @Test
    public void testApplyWithEmptyList_shouldDoNothing() {
        user.setTrackIdQueue(Collections.emptyList());
        final MusicUser result = strategy.apply(user);
        assertThat(result.getTrackIdQueue()).isEmpty();
        assertThat(result.getCurrentTrackIndex()).isEqualTo(0);
    }

    @Test
    public void testApplyWithOneItem_shouldDoNothing() {
        user.setTrackIdQueue(Collections.singletonList("0"));
        final MusicUser result = strategy.apply(user);
        assertThat(result.getTrackIdQueue()).hasSize(1);
        assertThat(result.getCurrentTrackIndex()).isEqualTo(0);
    }
}