package com.bwee.webit.datasource.entity;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class AlbumEntityTest {

    private Album album;

    @BeforeEach
    public void before() {
        album = new Album().setId("ALBUM1")
                .setName("A Sample Album")
                .setArtists(Collections.singletonList("Anonymous"))
                .setYear(2020)
                .setTags(Arrays.asList("Instrumental", "Classic"))
                .setTracks(Arrays.asList(
                        new Track().setId("2").setTrackNum("02"),
                        new Track().setId("1").setTrackNum("01"),
                        new Track().setId("3").setTrackNum("03")
                ));
    }

    @Test
    public void testCreate_shouldCreateEntity() {
        final AlbumEntity entity = new AlbumEntity(album);
        assertThat(entity.getId()).isEqualTo("ALBUM1");
        assertThat(entity.getName()).isEqualTo("A Sample Album");
        assertThat(entity.getArtists()).containsExactly("Anonymous");
        assertThat(entity.getYear()).isEqualTo(2020);
        assertThat(entity.getTags()).containsExactly("Instrumental", "Classic");
    }

    @Test
    public void testToModel_shouldReturnModel() {
        final AlbumEntity entity = new AlbumEntity(album);
        final Album model = entity.toModel();
        assertThat(model.getId()).isEqualTo(album.getId());
        assertThat(model.getName()).isEqualTo(album.getName());
        assertThat(model.getArtists()).isEqualTo(album.getArtists());
        assertThat(model.getYear()).isEqualTo(album.getYear());
        assertThat(model.getTags()).isEqualTo(album.getTags());
    }

    @Test
    public void testCopy_shouldCreateEqualObject() {
        final AlbumEntity entity = new AlbumEntity(album);
        final AlbumEntity copy = AlbumEntity.copyOf(entity);

        assertThat(copy).isEqualTo(entity);
        assertThat(copy).isEqualTo(AlbumEntity.copyOf(copy));
    }
}