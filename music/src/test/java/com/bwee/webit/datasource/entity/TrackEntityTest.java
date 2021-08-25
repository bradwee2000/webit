package com.bwee.webit.datasource.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TrackEntityTest {

    private TrackEntity music;

    @BeforeEach
    public void before() {
        music = new TrackEntity()
                .setId("ABC")
                .setTitle("It's My Life")
                .setAlbumName("XYZ=")
                .setTrackNum("0001")
                .setAlbumName("2020 Collection")
                .setArtists(Arrays.asList("John Snow"))
                .setComposer("Stark")
                .setExt("mp3")
                .setGenre(Collections.singletonList("Pop"))
                .setSize(3_000_000l)
                .setYear(2000)
                .setBitRate(10000)
                .setChannel("Stereo")
                .setDurationMillis(9_000_000l)
                .setSampleRate(200_000)
                .setOriginalAlbumName("2020_collection")
                .setOriginalArtist("Someone Unknown")
                .setImageUrl("http://img.com/XYZ")
                .setTags(List.of("Guitar", "Instrumental"));
    }

    @Test
    public void testCopy_shouldReturnEqualObject() {
        final TrackEntity copy = TrackEntity.copyOf(music);
        assertThat(copy).isEqualTo(music).isEqualTo(TrackEntity.copyOf(copy));
    }
}