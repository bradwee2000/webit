package com.bwee.webit.service.strategy.sort;

import com.bwee.webit.datasource.TrackDbService;
import com.bwee.webit.model.MusicUser;
import com.bwee.webit.model.Track;
import com.bwee.webit.service.strategy.sort.TrackNumSortStrategy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TrackNumSortStrategy.class)
class TrackNumSortStrategyTest {

    @Autowired
    private TrackNumSortStrategy strategy;

    @MockBean
    private TrackDbService trackDbService;

    private List<String> trackIds;
    private List<Track> tracks;
    private MusicUser user;

    @BeforeEach
    public void before() {
        int[] randomIds = {6, 5, 1, 3, 8, 7, 9, 0, 2, 4};
        trackIds = IntStream.of(randomIds).mapToObj(i -> String.valueOf(i)).collect(toList());
        tracks = IntStream.of(randomIds).mapToObj(i -> track(i)).collect(toList());

        when(trackDbService.findByIdsSorted(anyList())).thenReturn(tracks);

        user = new MusicUser().setCurrentTrackIndex(0).setTrackIdQueue(trackIds);
    }

    @Test
    public void testApply_shouldSortByTrackNum() {
        final MusicUser result = strategy.apply(user);
        assertThat(result.getTrackIdQueue()).containsExactly("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    }

    @Test
    public void testApply_shouldAdjustCurrentTrackNum() {
        user.setCurrentTrackIndex(4);
        final MusicUser result = strategy.apply(user);
        assertThat(result.getCurrentTrackIndex()).isEqualTo(8);
    }

    @Test
    public void testApplyMultipleTimes_shouldSortByTrackNum() {
        user.setCurrentTrackIndex(0);
        MusicUser result = strategy.apply(user);
        result = strategy.apply(result);
        result = strategy.apply(user);
        assertThat(result.getCurrentTrackIndex()).isEqualTo(6);
        assertThat(result.getTrackIdQueue()).containsExactly("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    }

    @Test
    public void testApplyWithEmptyList_shouldNotThrowException() {
        user.setTrackIdQueue(Collections.emptyList());
        user.setCurrentTrackIndex(3);
        final MusicUser result = strategy.apply(user);
        assertThat(result.getTrackIdQueue()).isEmpty();
        assertThat(result.getCurrentTrackIndex()).isEqualTo(0);
    }

    public Track track(int num) {
        return new Track().setId("" + num).setTrackNum("" + num);
    }
}