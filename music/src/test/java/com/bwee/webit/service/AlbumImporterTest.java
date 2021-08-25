package com.bwee.webit.service;

import com.bwee.webit.file.MusicFileService;
import com.bwee.webit.image.ImageService;
import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import com.bwee.webit.service.strategy.cover.AlbumCoverProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@Import(AlbumImporter.class)
@ContextConfiguration
class AlbumImporterTest {

    @Autowired
    private AlbumImporter albumImporter;

    @MockBean
    private TrackService trackService;

    @MockBean
    private Mp3Reader mp3Reader;

    @MockBean
    private FileService fileService;

    @MockBean
    private AlbumIdGenerator albumIdGenerator;

    @MockBean
    private TrackIdGenerator trackIdGenerator;

    @MockBean
    private ImageService imageService;

    @MockBean
    private MusicFileService musicFileService;

    @MockBean
    private AlbumImportCacheService albumCacheService;

    @Autowired
    private AlbumCoverProvider albumCoverProvider;

    private final Path importPath = Path.of(new File("src/test/resources/albums/Aretha Franklin").toURI());
    private final Path albumCoverPath = Path.of(importPath.toString(), "cover.jpg");
    private final String albumCoverUrl = "http://img.to/XYZ123";
    private Track track1, track2;

    @SneakyThrows
    @BeforeEach
    public void before() {
        track1 = new Track()
                .setArtists(singletonList("Anonymous"))
                .setAlbumName("Aretha Franklin")
                .setTrackNum("001")
                .setYear(2021)
                .setSourcePath(Path.of("/src/test/src/123.mp3"));
        track2 = new Track()
                .setArtists(singletonList("Le Vamp"))
                .setAlbumName("Aretha Franklin")
                .setTrackNum("002")
                .setYear(2021)
                .setSourcePath(Path.of("/src/test/src/456.mp3"));

        when(albumCoverProvider.provide(any(Path.class))).thenReturn(Optional.of("Hello".getBytes()));
        when(mp3Reader.readDir(any(Path.class), any(Mp3Reader.Config.class))).thenReturn(List.of(track1, track2));
        when(imageService.put(anyString(), any(byte[].class))).thenReturn(albumCoverUrl);
        when(albumIdGenerator.generateId(any(Album.class))).thenReturn("AAA");
        when(trackIdGenerator.generateId(any(Track.class))).thenReturn("123", "456");
        when(musicFileService.put(any(Path.class), any(Track.class))).thenReturn(
                Path.of("src/test/dest/123.mp3"),
                Path.of("src/test/dest/456.mp3")
        );
        when(albumCacheService.get(anyString())).thenReturn(Optional.empty());
    }

    @Test
    public void testImportWithDefaultConfig_shouldReturnAlbum() {
        final AlbumImporter.ImportConfig config = AlbumImporter.ImportConfig.defaults().path(importPath);
        final AlbumImporter.ImportedAlbum importedAlbum = albumImporter.importAlbumFromPath(config);
        final Album album = importedAlbum.getAlbum();

        assertThat(importedAlbum.isExisting()).isFalse();
        assertThat(album.getId()).isEqualTo("AAA");
        assertThat(album.getTracks()).containsExactly(track1, track2);
        assertThat(album.getOriginalName()).isEqualTo("Aretha Franklin");
        assertThat(album.getDisplayName()).isEqualTo("Aretha Franklin");
        assertThat(album.getYear()).isEqualTo(2021);
        assertThat(album.getArtists()).containsExactlyInAnyOrder("Anonymous", "Le Vamp");
        assertThat(album.getTags()).isEmpty();
        assertThat(album.getImageUrl()).isEqualTo("http://img.to/XYZ123");
    }

    @Test
    public void testImportWithDefaultConfig_shouldAssignTrackIds() {
        final AlbumImporter.ImportConfig config = AlbumImporter.ImportConfig.defaults().path(importPath);
        final AlbumImporter.ImportedAlbum importedAlbum = albumImporter.importAlbumFromPath(config);
        assertThat(importedAlbum.getAlbum().getTracks()).extracting(t -> t.getId()).containsExactly("123", "456");
    }

    @Test
    public void testImportWithDefaultConfig_shouldFormatTrackNums() {
        final AlbumImporter.ImportConfig config = AlbumImporter.ImportConfig.defaults().path(importPath);
        final AlbumImporter.ImportedAlbum importedAlbum = albumImporter.importAlbumFromPath(config);
        assertThat(importedAlbum.getAlbum().getTracks()).extracting(t -> t.getTrackNum()).containsExactly("1", "2");
    }

    @Test
    public void testImportWith10Tracks_shouldSortAndPadZerosToTrackNums() {
        final AlbumImporter.ImportConfig config = AlbumImporter.ImportConfig.defaults().path(importPath);
        final List<Track> tracks = IntStream.range(1, 11)
                .mapToObj(i -> new Track().setId("" + i).setTrackNum("" + i))
                .collect(Collectors.toList());

        Collections.shuffle(tracks, new Random(1));

        when(mp3Reader.readDir(any(Path.class), any(Mp3Reader.Config.class))).thenReturn(tracks);

        final AlbumImporter.ImportedAlbum importedAlbum = albumImporter.importAlbumFromPath(config);
        assertThat(importedAlbum.getAlbum().getTracks())
                .extracting(t -> t.getTrackNum())
                .containsExactly("01", "02", "03", "04", "05", "06", "07", "08", "09", "10");
    }

    @Test
    public void testImportWithOverwriteName_shouldReturnAlbumWithOverwrittenName() {
        final AlbumImporter.ImportConfig config =
                AlbumImporter.ImportConfig.defaults().path(importPath).albumName("New Hits");
        final AlbumImporter.ImportedAlbum importedAlbum = albumImporter.importAlbumFromPath(config);
        final Album album = importedAlbum.getAlbum();

        assertThat(album.getId()).isEqualTo("AAA");
        assertThat(album.getTracks()).containsExactly(track1, track2);
        assertThat(album.getOriginalName()).isEqualTo("Aretha Franklin");
        assertThat(album.getDisplayName()).isEqualTo("New Hits");
        assertThat(album.getYear()).isEqualTo(2021);
        assertThat(album.getArtists()).containsExactlyInAnyOrder("Anonymous", "Le Vamp");
    }

    @Test
    public void testImportWithOverwriteYear_shouldReturnAlbumWithOverwrittenYear() {
        final AlbumImporter.ImportConfig config =
                AlbumImporter.ImportConfig.defaults().path(importPath).albumYear(2000);
        final AlbumImporter.ImportedAlbum importedAlbum = albumImporter.importAlbumFromPath(config);
        final Album album = importedAlbum.getAlbum();

        assertThat(album.getId()).isEqualTo("AAA");
        assertThat(album.getTracks()).containsExactly(track1, track2);
        assertThat(album.getOriginalName()).isEqualTo("Aretha Franklin");
        assertThat(album.getDisplayName()).isEqualTo("Aretha Franklin");
        assertThat(album.getYear()).isEqualTo(2000);
        assertThat(album.getTracks()).extracting(t -> t.getYear()).containsExactly(2000, 2000);
        assertThat(album.getArtists()).containsExactlyInAnyOrder("Anonymous", "Le Vamp");
    }

    @Test
    public void testImportWithCopyFiles_shouldCopyAlbumCover() {
        final AlbumImporter.ImportConfig config =
                AlbumImporter.ImportConfig.defaults().path(importPath).copyFiles(true);
        final AlbumImporter.ImportedAlbum importedAlbum = albumImporter.importAlbumFromPath(config);

        assertThat(importedAlbum.getAlbum().getId()).isEqualTo("AAA");
        assertThat(importedAlbum.getAlbum().getImageUrl()).isEqualTo(albumCoverUrl);
        assertThat(track1.getImageUrl()).isEqualTo(albumCoverUrl);
        assertThat(track2.getImageUrl()).isEqualTo(albumCoverUrl);
    }

    @Test
    public void testImportWithCopyFiles_shouldSetTracksNewSourcePath() {
        final AlbumImporter.ImportConfig config =
                AlbumImporter.ImportConfig.defaults().path(importPath).copyFiles(true);
        final AlbumImporter.ImportedAlbum importedAlbum = albumImporter.importAlbumFromPath(config);

        assertThat(importedAlbum.getAlbum().getId()).isEqualTo("AAA");
        assertThat(track1.getSourcePath()).isEqualTo(Path.of("src/test/dest/123.mp3"));
        assertThat(track2.getSourcePath()).isEqualTo(Path.of("src/test/dest/456.mp3"));
    }

    @Test
    @SneakyThrows
    public void testImportExistingAlbum_shouldNotImport() {
        final Album existingAlbum = new Album().setId("AAA");
        when(albumCacheService.get("AAA")).thenReturn(Optional.of(existingAlbum));

        final AlbumImporter.ImportConfig config = AlbumImporter.ImportConfig.defaults()
                .path(importPath)
                .copyFiles(true);
        final AlbumImporter.ImportedAlbum importedAlbum = albumImporter.importAlbumFromPath(config);

        assertThat(importedAlbum.getAlbum().getId()).isEqualTo("AAA");
        assertThat(importedAlbum.isExisting()).isTrue();

        // Should not copy files
        verify(musicFileService, never()).put(any(), any());
        verify(trackService, never()).saveAll(any());
        verify(albumCacheService, never()).put(any());
    }

    @Test
    @SneakyThrows
    public void testImportExistingAlbumWithOverwrite_shouldImport() {
        final Album existingAlbum = new Album().setId("AAA");
        when(albumCacheService.get(anyString())).thenReturn(Optional.of(existingAlbum));

        final AlbumImporter.ImportConfig config = AlbumImporter.ImportConfig.defaults().path(importPath)
                .copyFiles(true)
                .overwriteExisting(true);

        final AlbumImporter.ImportedAlbum importedAlbum = albumImporter.importAlbumFromPath(config);
        final Album album = importedAlbum.getAlbum();

        assertThat(album.getId()).isEqualTo("AAA");
        assertThat(importedAlbum.isExisting()).isTrue();

        // Should copy files
        verify(musicFileService, atLeastOnce()).put(any(), any());
        verify(trackService, times(1)).saveAll(any());
        verify(albumCacheService, times(1)).put(any());
    }

    @Configuration
    public static class Ctx {
        @Bean
        public AlbumCoverProvider albumCoverProvider() {
            return mock(AlbumCoverProvider.class);
        }

        @Bean
        public List<AlbumCoverProvider> albumCoverProviders() {
            return Arrays.asList(albumCoverProvider());
        }
    }
}