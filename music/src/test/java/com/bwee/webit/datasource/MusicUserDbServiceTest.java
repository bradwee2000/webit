package com.bwee.webit.datasource;

import com.bwee.webit.config.CassandraConfiguration;
import com.bwee.webit.model.MusicUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@EmbeddedCassandra
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "spring.data.cassandra.keyspace-name=\"testwebit\"",
        "spring.data.cassandra.port=9142",
        "spring.data.cassandra.schema-action=RECREATE_DROP_UNUSED"
})
@ContextConfiguration(classes = {MusicUserDbService.class, CassandraConfiguration.class})
class MusicUserDbServiceTest {

    private MusicUser johnOriginal, johnClone;

    @Autowired
    private MusicUserDbService musicUserDbService;

    @BeforeAll
    @SneakyThrows
    public static void beforeAll() {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(10_000L);
    }

    @BeforeEach
    public void before() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();

        johnOriginal = new MusicUser().setId("123")
                .setName("John Le Original")
                .setCurrentTrackIndex(3)
                .setTrackIdQueue(List.of("A", "B", "C"));

        johnClone = MusicUser.copyOf(johnOriginal)
                .setName("John Le Clone")
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
                .hasValueSatisfying(user -> assertThat(user.getCurrentTrackIndex()).isEqualTo(2))
                .hasValueSatisfying(user -> assertThat(user.getName()).isEqualTo("John Le Original"));
    }

    @Test
    public void testUpdateTrackIds_shouldUpdateOnlyTrackIds() {
        musicUserDbService.updateTrackIds(johnClone);
        assertThat(musicUserDbService.findById("123")).hasValue(
                johnOriginal
                        .setTrackIdQueue(johnClone.getTrackIdQueue())
                        .setCurrentTrackIndex(0)
        );
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