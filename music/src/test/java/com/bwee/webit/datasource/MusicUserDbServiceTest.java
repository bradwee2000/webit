package com.bwee.webit.datasource;

import com.bwee.webit.model.MusicUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ContextConfiguration(classes = { MusicUserDbService.class })
class MusicUserDbServiceTest extends EmbeddedCassandraTest {

    private MusicUser johnOriginal, johnClone;

    @Autowired
    private MusicUserDbService musicUserDbService;

    @BeforeEach
    public void before() {
        johnOriginal = new MusicUser().setId("123")
                .setCurrentTrackIndex(3)
                .setTrackIdQueue(List.of("A", "B", "C"));

        johnClone = MusicUser.copyOf(johnOriginal)
                .setCurrentTrackIndex(2)
                .setTrackIdQueue(List.of("D", "E"))
                .setLoop(true)
                .setShuffle(true)
                .setPlaying(true);

        musicUserDbService.save(johnOriginal);
    }

    @Test
    public void testFind_shouldReturnEqualValue() {
        assertThat(musicUserDbService.findById("123")).hasValue(johnOriginal);
    }

    @Test
    public void testUpdateCurrentTrackIndex_shouldUpdateOnlyTrackIndex() {
        musicUserDbService.updateCurrentTrackIndex(johnClone);

        assertThat(musicUserDbService.findById("123"))
                .hasValueSatisfying(user -> assertThat(user.getCurrentTrackIndex()).isEqualTo(2));
    }

    @Test
    public void testUpdateLoop_shouldUpdateOnlyLoop() {
        musicUserDbService.updateLoop(johnClone);
        assertThat(musicUserDbService.findById("123")).hasValue(johnOriginal.setLoop(true));
    }

    @Test
    public void testUpdateShuffle_shouldUpdateOnlyShuffleTrackQueueAndIndex() {
        musicUserDbService.updateShuffle(johnClone);
        assertThat(musicUserDbService.findById("123")).hasValue(
                johnOriginal.setShuffle(true)
                        .setCurrentTrackIndex(2)
                        .setTrackIdQueue(List.of("D", "E"))
        );
    }
}