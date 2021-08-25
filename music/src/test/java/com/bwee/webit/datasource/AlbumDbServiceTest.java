package com.bwee.webit.datasource;

import com.bwee.webit.model.Album;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = AlbumDbService.class)
class AlbumDbServiceTest extends EmbeddedCassandraTest {

    @Autowired
    private AlbumDbService dbService;

    private Album album;

    @BeforeEach
    public void before() {
        album = new Album()
                .setOriginalName("Greatest Hits 2020!!")
                .setDisplayName("Greatest Hits 2020")
                .setId("ABC")
                .setYear(2020)
                .setTags(List.of("Piano", "Instrumental"));
    }

    @Test
    public void testSaveAndFindById_shouldReturnEqualResult() {
        dbService.save(album);

        final Album album = dbService.findById("ABC").get();

        assertThat(album.getId()).isEqualTo("ABC");
        assertThat(album.getYear()).isEqualTo(2020);
        assertThat(album.getOriginalName()).isEqualTo("Greatest Hits 2020!!");
        assertThat(album.getDisplayName()).isEqualTo("Greatest Hits 2020");
        assertThat(album.getTags()).containsExactlyInAnyOrder("Piano", "Instrumental");
    }

    @Test
    public void testFindByUnknownId_shouldReturnEmpty() {
        assertThat(dbService.findById("UNKNOWN")).isEmpty();
    }

//    @Test
//    public void testMerge_shouldMergeLists() {
//        final AlbumEntity newEntity = new AlbumEntity()
//                .setName("Greatest Hits 2021")
//                .setId("ABC")
//                .setYear(2020).setTags(List.of("Instrumental", "Happy"))
//                .setTracks(List.of(
//                        new AlbumEntity.Track("A5"),
//                        new AlbumEntity.Track("A3"),
//                        new AlbumEntity.Track("A4")
//                ));
//
//        final AlbumEntity mergedEntity = albumDbService.merge(album, newEntity);
//
//        assertThat(mergedEntity.getName()).isEqualTo("Greatest Hits 2021");
//        assertThat(mergedEntity.getTags()).containsExactlyInAnyOrder("Piano", "Instrumental", "Happy");
//        assertThat(mergedEntity.getTracks()).extracting(t -> t.getTrackId()).containsExactly("A1", "A2", "A3", "A4", "A5");
//    }
}