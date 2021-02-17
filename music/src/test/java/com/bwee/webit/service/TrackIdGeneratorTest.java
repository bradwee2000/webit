package com.bwee.webit.service;


import com.bwee.webit.config.MusicConfiguration;
import com.bwee.webit.model.Genre;
import com.bwee.webit.model.Track;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@Import(MusicConfiguration.class)
public class TrackIdGeneratorTest {

    @Autowired
    private TrackIdGenerator generator;
    private Track track;

    @Before
    public void before() {
        track = new Track()
                .setTitle("This is the LIFE")
                .setArtist("Big Hero")
                .setComposer("John Snow")
                .setExt("mp3")
                .setGenre(singletonList(Genre.POP.getName()));
    }

    @Test
    public void testGenerateId_shouldGenerateId() {
        final String id = generator.generateId(track);
        assertThat(id).isNotEmpty();
    }

    @Test
    public void testGenerateIdMultipleTimesForSameObject_shouldGenerateEqualId() {
        final String id = generator.generateId(track);
        assertThat(id).isEqualTo(generator.generateId(track));
        assertThat(generator.generateId(track)).isEqualTo(generator.generateId(track));
    }

    @Test
    public void testGenerateIdForDuplicateObjects_shouldGenerateEqualId() {
        final Track duplicate = new Track()
                .setTitle("This is the LIFE")
                .setArtist("Big Hero")
                .setComposer("John Snow")
                .setExt("mp3")
                .setGenre(singletonList(Genre.POP.getName()));

        assertThat(generator.generateId(track)).isEqualTo(generator.generateId(duplicate));
    }

    @Test
    public void testGenerateIdsForMusicWithDiffTitle_shouldGenerateUniqueIds() {
        assertThat(generator.generateId(track)).isNotEqualTo(generator.generateId(track.setTitle("This is the LIFE!")));
    }

    @Test
    public void testGenerateIdsForMusicWithDiffArtist_shouldGenerateUniqueIds() {
        assertThat(generator.generateId(track)).isNotEqualTo(generator.generateId(track.setArtist("Big Hero 6")));
    }

    @Test
    public void testGenerateIdsForMusicWithDiffComposer_shouldGenerateEqualIds() {
        assertThat(generator.generateId(track)).isEqualTo(generator.generateId(track.setComposer("A new Composer!")));
    }

    @Test
    public void testGenerateIdsForMusicWithDiffCharacterCase_shouldGenerateEqualIds() {
        assertThat(generator.generateId(track)).isEqualTo(generator.generateId(track.setTitle("This is the life")));
        assertThat(generator.generateId(track)).isEqualTo(generator.generateId(track.setArtist("BIG HERO")));
        assertThat(generator.generateId(track)).isEqualTo(generator.generateId(track.setComposer("John SNOW")));
        assertThat(generator.generateId(track)).isEqualTo(generator.generateId(track.setExt("MP3")));
    }

    @Test
    public void testGenerateIdsForMultipleMusic_shouldNotProduceCollisions() {
        final Set<String> keys = new HashSet<>();
        final int size = 100_000;

        for (int i=0; i< size; i++) {
            track.setTitle(RandomStringUtils.randomAlphanumeric(3, 10));
            track.setArtist(RandomStringUtils.randomAlphanumeric(5, 10));
            final String id = generator.generateId(track);
            if (i % 100_000 == 0){
                assertThat(keys).hasSize(i);
            }
            keys.add(id);
        }
        assertThat(keys).hasSize(size);
    }
}