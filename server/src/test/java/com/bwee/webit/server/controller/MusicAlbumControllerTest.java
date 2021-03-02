package com.bwee.webit.server.controller;

import com.bwee.webit.exception.AlbumContentNotFoundException;
import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import com.bwee.webit.server.model.ImportAlbum;
import com.bwee.webit.service.AlbumService;
import com.bwee.webit.service.MusicAlbumImporter;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.bwee.webit.server.util.TestUtils.om;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MusicAlbumController.class)
class MusicAlbumControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    AlbumService albumService;

    @MockBean
    private MusicAlbumImporter musicAlbumImporter;

    private Album album;
    private Track alphabet;
    private Track littleLamb;

    @BeforeEach
    public void before() {
        alphabet = new Track().setId("M1").setTitle("Alphabet");
        littleLamb = new Track().setId("M2").setTitle("Little Lamb");

        album = new Album().setId("ABC")
                .setName("Greatest Kids Hits")
                .setYear(2000)
                .setTags(List.of("Children", "Happy"))
                .setTracks(List.of(alphabet, littleLamb));

        when(albumService.findByIdStrict("ABC")).thenReturn(album);
        when(albumService.findById("ABC")).thenReturn(Optional.of(album));
        when(albumService.findAll(any())).thenReturn(new SliceImpl<>(singletonList(album)));
    }

    @Test
    @SneakyThrows
    public void testGetById_shouldReturnAlbumWithGivenId() {
        mvc.perform(get("/music/albums/ABC"))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"id\":\"ABC\"," +
                        "\"name\":\"Greatest Kids Hits\"," +
                        "\"musicList\":[" +
                        "   {\"id\":\"M1\",\"title\":\"Alphabet\"}," +
                        "   {\"id\":\"M2\",\"title\":\"Little Lamb\"}]," +
                        "   \"year\":2000," +
                        "   \"tags\":[\"Children\",\"Happy\"]}"));
    }

    @Test
    @SneakyThrows
    public void testGetList_shouldReturnList() {
        final String result = mvc.perform(get("/music/albums"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Album> albums = om().readValue(result, new TypeReference<>() {});
        assertThat(albums).hasSize(1).containsExactly(album);
    }

    @Test
    @SneakyThrows
    public void testGetByUnknownId_shouldShowNotFound() {
        when(albumService.findByIdStrict(any())).thenThrow(new AlbumContentNotFoundException("XYZ"));
        mvc.perform(get("/music/albums/XYZ"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Content is not found. id=XYZ, source=album\"}"));
    }

    @Test
    @SneakyThrows
    public void testImport_shouldImportedAlbum() {
        when(musicAlbumImporter.importAlbumFromPath(any(), anyString(), any(), anyInt(), anyBoolean(), anyBoolean())).thenReturn(album);
        final ImportAlbum req = new ImportAlbum().setName("Alphabets 2000").setPath("/x/y/z").setYear(2000);

        mvc.perform(post("/music/albums/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om().writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":\"ABC\",\"name\":\"Greatest Kids Hits\"}"))
                .andExpect(header().string("Location", "/music/albums/ABC"));
    }
}