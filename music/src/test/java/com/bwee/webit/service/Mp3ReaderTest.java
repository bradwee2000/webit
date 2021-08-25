package com.bwee.webit.service;

import com.bwee.webit.config.MusicConfiguration;
import com.bwee.webit.model.Track;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class Mp3ReaderTest {

    private Mp3Reader reader;

    @BeforeEach
    public void before() {
        reader = new MusicConfiguration().mp3Reader();
    }

    @Test
    public void testRead_shouldReturnListOfTracks() {
        final String albumPath = Paths.get("src","test","resources", "albums", "Aretha Franklin").toString();
        final List<Track> trackList = reader.readDir(albumPath).stream()
                .sorted(Comparator.comparing(Track::getTrackNum))
                .collect(Collectors.toList());
        assertThat(trackList).extracting(t -> t.getTitle()).containsExactly("I Told You So (Remastered)");
        assertThat(trackList).extracting(t -> t.getAlbumName()).containsExactly("Aretha Franklin Retrospective (All Tracks Remastered)");
        assertThat(trackList).flatExtracting(t -> t.getArtists()).containsExactly("Aretha Franklin");
        trackList.forEach(m -> log.info(" - {}", m));
    }

    @Test
    public void test() {
        final String albumPath = Paths.get("src","test","resources", "albums", "Black Coffee").toString();

        final List<Track> trackList = reader.readDir(albumPath).stream()
                .sorted(Comparator.comparing(Track::getTrackNum))
                .collect(Collectors.toList());

        trackList.forEach(m -> log.info(" - {}", m));
    }
}