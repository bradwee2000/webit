package com.bwee.webit.search.model;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

class AlbumDocumentTest {

    private Album album;

    @BeforeEach
    public void before() {
        album = new Album().setId("ALBUM1")
                .setOriginalName("A Sample Album!!")
                .setDisplayName("A Sample Album")
                .setArtists(singletonList("Anonymous"))
                .setYear(2020)
                .setTags(Arrays.asList("Instrumental", "Classic"))
                .setTracks(Arrays.asList(
                        new Track().setId("2").setTrackNum("002").setArtists(singletonList("Sam")).setTitle("Hello"),
                        new Track().setId("1").setTrackNum("001").setArtists(singletonList("Sam")).setTitle("Beautiful"),
                        new Track().setId("3").setTrackNum("003").setArtists(singletonList("Ivy")).setTitle("World")
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
        assertThat(model.getDisplayName()).isEqualTo("A Sample Album");
        assertThat(model.getArtists()).containsExactly("Anonymous");
        assertThat(model.getYear()).isEqualTo(2020);
        assertThat(model.getTags()).containsExactly("Instrumental", "Classic");
    }
}