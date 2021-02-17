package com.bwee.webit.service;

import com.bwee.webit.config.MusicConfiguration;
import com.bwee.webit.model.Track;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest(classes = MusicConfiguration.class)
public class Mp3ReaderTest {

    @Autowired
    private Mp3Reader reader;

    @Test
    public void test() {
        final List<Track> trackList = reader.readDir("/Users/bradfordwee/Downloads/Music/Best of Country 2020 (Mp3 320kbps) [PMEDIA] ⭐️").stream()
                .sorted(Comparator.comparing(Track::getTrack))
                .collect(Collectors.toList());
        trackList.forEach(m -> log.info(" - {}", m));
    }
}