package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.TrackEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TrackDbService.class)
class TrackDbServiceTest {

    @Autowired
    private TrackDbService trackDbService;

    @MockBean
    private CassandraOperations cassandraOperations;

    private TrackEntity music;

    @BeforeEach
    public void before() {
        music = new TrackEntity()
                .setId("ABC")
                .setTitle("It's My Life")
                .setArtist("John Snow")
                .setComposer("Stark")
                .setExt("mp3")
                .setGenre(Collections.singletonList("Pop"))
                .setSize(3_000_000l)
                .setYear(2000)
                .setTags(List.of("Guitar", "Instrumental"));
    }

    @Test
    public void testMerge_shouldMergeTagsAndGenre() {
        final TrackEntity newMusic = new TrackEntity()
                .setId("ABC")
                .setTitle("It's My Life!")
                .setArtist("John de la Snow")
                .setComposer("Stark")
                .setExt("mp3")
                .setGenre(List.of("Pop", "Rock"))
                .setSize(3_000_000l)
                .setYear(2000).setTags(List.of("Instrumental", "Snow"));

        final TrackEntity mergedMusic = trackDbService.merge(music, newMusic);
        assertThat(mergedMusic.getTitle()).isEqualTo("It's My Life!");
        assertThat(mergedMusic.getArtist()).isEqualTo("John de la Snow");
        assertThat(mergedMusic.getGenre()).containsExactlyInAnyOrder("Pop", "Rock");
        assertThat(mergedMusic.getTags()).containsExactlyInAnyOrder("Guitar", "Instrumental", "Snow");
    }
}