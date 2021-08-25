package com.bwee.webit.datasource;

import com.bwee.webit.model.Track;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ContextConfiguration(classes = { TrackDbService.class})
class TrackDbServiceTest extends EmbeddedCassandraTest {

    @Autowired
    private TrackDbService dbService;

    private Track track1, track2, track3;

    private Track trackA;

    @BeforeEach
    public void before() {
        track1 = new Track()
                .setId("ABC")
                .setTitle("It's My Life")
                .setArtists(Arrays.asList("John Snow"))
                .setComposer("Stark")
                .setExt("mp3")
                .setTrackNum("01")
                .setAlbumId("AAA")
                .setAlbumName("Greatest Hits")
                .setGenre(Collections.singletonList("Pop"))
                .setSize(3_000_000l)
                .setYear(2000)
                .setTags(List.of("Guitar", "Instrumental"));

        track2 = new Track().setId("DEF").setAlbumId("AAA").setTrackNum("02");
        track3 = new Track().setId("GHI").setAlbumId("AAA").setTrackNum("03");
        trackA = new Track().setId("JKL").setAlbumId("BBB").setTrackNum("01");
    }

    @Test
    public void testSaveAndFindById_shouldReturnEqualResult() {
        dbService.save(track1);
        assertThat(dbService.findById("ABC")).hasValue(track1);
    }

    @Test
    public void testFindAllByIds_shouldReturnAllTracksWithGivenIds() {
        dbService.save(trackA);
        dbService.save(track1);
        dbService.save(track2);

        assertThat(dbService.findByIds(List.of("ABC", "DEF"))).containsExactlyInAnyOrder(track1, track2);
        assertThat(dbService.findByIds(List.of("ABC", "DEF", "JKL"))).containsExactlyInAnyOrder(track1, track2, trackA);
    }

    @Test
    public void testFindByAlbumId_shouldReturnTracksInAlbum() {
        dbService.save(trackA);
        dbService.save(track3);
        dbService.save(track1);
        dbService.save(track2);

        assertThat(dbService.findByAlbumId("AAA")).containsExactlyInAnyOrder(track1, track2, track3);
        assertThat(dbService.findByAlbumId("BBB")).containsExactly(trackA);
    }

    @Test
    public void testFindByUnknownAlbumId_shouldReturnEmptyList() {
        assertThat(dbService.findByAlbumId("UNKNOWN")).isEmpty();
    }

    @Test
    public void testFindById_shouldReturnTrack() {
        dbService.save(track1);
        dbService.save(track2);

        assertThat(dbService.findById("ABC")).hasValue(track1);
        assertThat(dbService.findById("DEF")).hasValue(track2);
    }

    @Test
    public void testFindByUnknownId_shouldReturnEmpty() {
        assertThat(dbService.findById("UNKNOWN")).isEmpty();
    }

    @Test
    public void testFindByAlbumIdWith100Tracks_shouldReturn100Tracks() {
        final List<Track> tracks = IntStream.range(1, 101)
                .mapToObj(i -> new Track()
                        .setId("IDAKDJLDKEIDKSJ" + i)
                        .setTrackNum("00" + i)
                        .setAlbumId("AN_ALBUM_ID")
                        .setAlbumName("Greatest Hits of All time")
                        .setArtists(Arrays.asList("Anonymous"))
                        .setDurationMillis(180_000)
                        .setYear(2000)
                        .setTitle("Song number: " + i))
                .collect(Collectors.toList());
        dbService.saveAll(tracks);

        assertThat(dbService.findByAlbumId("AN_ALBUM_ID")).hasSize(100);
    }
}