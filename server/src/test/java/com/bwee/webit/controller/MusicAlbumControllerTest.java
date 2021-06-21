package com.bwee.webit.controller;

import com.bwee.webit.exception.AlbumNotFoundException;
import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import com.bwee.webit.server.auth.AuthFilter;
import com.bwee.webit.server.config.SecurityConfiguration;
import com.bwee.webit.server.controller.MusicAlbumController;
import com.bwee.webit.server.model.music.AlbumTracksRes;
import com.bwee.webit.server.model.music.TrackRes;
import com.bwee.webit.server.service.AlbumTrackResFactory;
import com.bwee.webit.server.service.MusicUserResFactory;
import com.bwee.webit.service.AlbumService;
import com.bwee.webit.service.MusicUserService;
import com.bwee.webit.service.TrackService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bwee.webit.util.TestUtils.om;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MusicAlbumController.class,
        excludeAutoConfiguration = { SecurityAutoConfiguration.class},
        excludeFilters = {
            @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes = { SecurityConfiguration.class, AuthFilter.class })
        })
class MusicAlbumControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AlbumService albumService;

    @MockBean
    private MusicUserService musicUserService;

    @MockBean
    private MusicUserResFactory musicUserResFactory;

    @MockBean
    private TrackService trackService;

    @MockBean
    private AlbumTrackResFactory albumTrackResFactory;

    private Album album;
    private AlbumTracksRes albumTracksRes;
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

        albumTracksRes = new AlbumTracksRes()
                .setId(album.getId())
                .setName(album.getName())
                .setTags(album.getTags())
                .setYear(album.getYear())
                .setTracks(album.getTracks().stream().map(TrackRes::new).collect(Collectors.toList()));

        when(albumService.findByIdStrict("ABC")).thenReturn(album);
        when(albumService.findById("ABC")).thenReturn(Optional.of(album));
        when(albumService.findAll(any())).thenReturn(new SliceImpl<>(singletonList(album)));
        when(albumTrackResFactory.build(album)).thenReturn(albumTracksRes);
    }

    @Test
    @SneakyThrows
    public void testGetById_shouldReturnAlbumWithGivenId() {
        mvc.perform(get("/music/albums/ABC"))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"id\":\"ABC\"," +
                        "\"name\":\"Greatest Kids Hits\"," +
                        "\"tracks\":[" +
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

        final List<Album> albums = om().readValue(result, new TypeReference<>() {});
        assertThat(albums).hasSize(1).containsExactly(album);
    }

    @Test
    @SneakyThrows
    public void testGetByUnknownId_shouldShowNotFound() {
        when(albumService.findByIdStrict(any())).thenThrow(new AlbumNotFoundException("XYZ"));
        mvc.perform(get("/music/albums/XYZ"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Content is not found. id=XYZ, source=album\"}"));
    }

    @Test
    @SneakyThrows
    public void testUpdateById_shouldUpdateAlbum() {
        final String jsonPayload = "{\"name\": \"Rock\", \"tags\": [\"paper\"], \"year\": 1992 }";

        mvc.perform(put("/music/albums/ABC")
                .content(jsonPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"ABC\"}"));

        final ArgumentCaptor<Album> captor = ArgumentCaptor.forClass(Album.class);
        verify(albumService).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo("Rock");
        assertThat(captor.getValue().getTags()).containsExactly("paper");
        assertThat(captor.getValue().getYear()).isEqualTo(1992);
    }

    @Test
    @SneakyThrows
    public void testPartialUpdateById_shouldUpdateAlbumPartially() {
        final String jsonPayload = "{\"name\": \"Rock\"}";

        mvc.perform(put("/music/albums/ABC")
                .content(jsonPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"ABC\"}"));

        final ArgumentCaptor<Album> captor = ArgumentCaptor.forClass(Album.class);
        verify(albumService).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo("Rock");
        assertThat(captor.getValue().getTags()).containsExactly("Children", "Happy");
        assertThat(captor.getValue().getYear()).isEqualTo(2000);
    }
}