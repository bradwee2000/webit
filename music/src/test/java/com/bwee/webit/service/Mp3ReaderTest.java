package com.bwee.webit.service;

import com.bwee.webit.model.Track;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Mp3ReaderTest {

    private Mp3Reader reader;

    @BeforeEach
    public void before() {
        reader = new Mp3Reader();
    }

    @Test
    public void testRead_shouldReturnListOfTracks() {
        final List<Track> trackList = reader.readDir("/Users/bradfordwee/Downloads/Music/Best of Country 2020 (Mp3 320kbps) [PMEDIA] ⭐️").stream()
                .sorted(Comparator.comparing(Track::getTrackNum))
                .collect(Collectors.toList());
        trackList.forEach(m -> log.info(" - {}", m));
    }
}