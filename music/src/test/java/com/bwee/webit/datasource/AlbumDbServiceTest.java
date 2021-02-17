package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.AlbumEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AlbumDbService.class)
class AlbumDbServiceTest {

    @Autowired
    private AlbumDbService albumDbService;

    @MockBean
    private CassandraOperations cassandraOperations;

    private AlbumEntity album;

    @BeforeEach
    public void before() {
        album = new AlbumEntity()
                .setName("Greatest Hits 2020")
                .setId("ABC")
                .setYear(2020)
                .setTags(List.of("Piano", "Instrumental"))
                .setTracks(List.of(
                        new AlbumEntity.Track("A1", 1),
                        new AlbumEntity.Track("A3", 3),
                        new AlbumEntity.Track("A2", 2)
                ));
    }

    @Test
    public void testMerge_shouldMergeLists() {
        final AlbumEntity newEntity = new AlbumEntity()
                .setName("Greatest Hits 2021")
                .setId("ABC")
                .setYear(2020).setTags(List.of("Instrumental", "Happy"))
                .setTracks(List.of(
                        new AlbumEntity.Track("A5", 4),
                        new AlbumEntity.Track("A3", 5),
                        new AlbumEntity.Track("A4", 1)
                ));

        final AlbumEntity mergedEntity = albumDbService.merge(album, newEntity);

        assertThat(mergedEntity.getName()).isEqualTo("Greatest Hits 2021");
        assertThat(mergedEntity.getTags()).containsExactlyInAnyOrder("Piano", "Instrumental", "Happy");
        assertThat(mergedEntity.getTracks()).extracting(t -> t.getMusicId()).containsExactly("A1", "A2", "A3", "A4", "A5");
        assertThat(mergedEntity.getTracks()).extracting(t -> t.getTrackNum()).containsExactly(1, 2, 3, 4, 5);
    }
}