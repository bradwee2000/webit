package com.bwee.webit.server.controller;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import com.bwee.webit.server.auth.AuthFilter;
import com.bwee.webit.server.config.SecurityConfiguration;
import com.bwee.webit.server.controller.MusicImportController;
import com.bwee.webit.server.model.music.ImportAlbumReq;
import com.bwee.webit.service.AlbumImporter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.bwee.webit.util.TestUtils.om;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MusicImportController.class,
        excludeAutoConfiguration = { SecurityAutoConfiguration.class},
        excludeFilters = {
                @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes = { SecurityConfiguration.class, AuthFilter.class })
        })
class MusicImportControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AlbumImporter albumImporter;

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
    }

    @Test
    @SneakyThrows
    public void testImport_shouldImportedAlbum() {
        when(albumImporter.importAlbumFromPath(any())).thenReturn(album);
        final ImportAlbumReq req = new ImportAlbumReq().setName("Alphabets 2000").setPath("/x/y/z").setYear(2000);

        mvc.perform(post("/music/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om().writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":\"ABC\",\"name\":\"Greatest Kids Hits\"}"))
                .andExpect(header().string("Location", "/music/albums/ABC"));
    }
}