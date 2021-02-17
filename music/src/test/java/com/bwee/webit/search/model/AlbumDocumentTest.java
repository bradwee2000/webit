package com.bwee.webit.search.model;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class AlbumDocumentTest {

    private Album album;

    @BeforeEach
    public void before() {
        album = new Album().setId("ALBUM1")
                .setName("A Sample Album")
                .setArtists(Collections.singletonList("Anonymous"))
                .setYear(2020)
                .setTags(Arrays.asList("Instrumental", "Classic"))
                .setTracks(Arrays.asList(
                        new Track().setId("2").setTrack(2).setArtist("Sam").setTitle("Hello"),
                        new Track().setId("1").setTrack(1).setArtist("Sam").setTitle("Beautiful"),
                        new Track().setId("3").setTrack(3).setArtist("Ivy").setTitle("World")
                ));
    }

    @Test
    public void testCreate_shouldCreateEntity() {
        final AlbumDocument doc = new AlbumDocument(album);
        assertThat(doc.getId()).isEqualTo("ALBUM1");
        assertThat(doc.getName()).isEqualTo("A Sample Album");
        assertThat(doc.getArtists()).containsExactly("Anonymous");
        assertThat(doc.getYear()).isEqualTo(2020);
        assertThat(doc.getTags()).containsExactly("Instrumental", "Classic");
        assertThat(doc.getTrackNames()).containsExactly("Sam Hello", "Sam Beautiful", "Ivy World");
    }

    @Test
    public void testToModel_shouldCreateModel() {
        final AlbumDocument doc = new AlbumDocument(album);
        final Album model = doc.toModel();
        assertThat(model.getName()).isEqualTo("A Sample Album");
        assertThat(model.getArtists()).containsExactly("Anonymous");
        assertThat(model.getYear()).isEqualTo(2020);
        assertThat(model.getTags()).containsExactly("Instrumental", "Classic");
    }
}