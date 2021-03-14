package com.bwee.webit.service;

import com.bwee.webit.file.MusicFileService;
import com.bwee.webit.image.ImageService;
import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(SpringExtension.class)
@Import(AlbumImporter.class)
class AlbumImporterTest {

    @Autowired
    private AlbumImporter albumImporter;

    @MockBean
    private TrackService trackService;

    @MockBean
    private AlbumService albumService;

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

    private final Path importPath = Path.of(new File("src/test/resources/albums/Aretha Franklin").toURI());
    private final Path albumCoverPath = Path.of(importPath.toString(), "cover.jpg");
    private final String albumCoverUrl = "http://img.to/XYZ123";
    private Track track1, track2;

    @BeforeEach
    public void before() {
        track1 = new Track()
                .setArtist("Anonymous")
                .setAlbumName("Aretha Franklin")
                .setTrackNum("001")
                .setYear(2021)
                .setSourcePath(Path.of("/src/test/src/123.mp3"));
        track2 = new Track()
                .setArtist("Le Vamp")
                .setAlbumName("Aretha Franklin")
                .setTrackNum("002")
                .setYear(2021)
                .setSourcePath(Path.of("/src/test/src/456.mp3"));

        when(mp3Reader.readDir(any(Path.class), any(Mp3Reader.Config.class))).thenReturn(List.of(track1, track2));
        when(mp3Reader.albumCover(importPath)).thenReturn(new Mp3Reader.AlbumCover().setPath(albumCoverPath));
        when(imageService.put(anyString(), any(byte[].class))).thenReturn(albumCoverUrl);
        when(albumIdGenerator.generateId(any(Album.class))).thenReturn("AAA");
        when(trackIdGenerator.generateId(any(Track.class))).thenReturn("123", "456");
        when(musicFileService.put(any(Path.class), any(Track.class))).thenReturn(
                Path.of("src/test/dest/123.mp3"),
                Path.of("src/test/dest/456.mp3")
        );
    }

    @Test
    public void testImportWithDefaultConfig_shouldReturnAlbum() {
        final AlbumImporter.ImportConfig config = AlbumImporter.ImportConfig.defaults().path(importPath);
        final Album album = albumImporter.importAlbumFromPath(config);

        assertThat(album.getId()).isEqualTo("AAA");
        assertThat(album.getTracks()).containsExactly(track1, track2);
        assertThat(album.getName()).isEqualTo("Aretha Franklin");
        assertThat(album.getYear()).isEqualTo(2021);
        assertThat(album.getArtists()).containsExactlyInAnyOrder("Anonymous", "Le Vamp");
        assertThat(album.getTags()).isEmpty();
        assertThat(album.getImageUrl()).isNull();
    }

    @Test
    public void testImportWithDefaultConfig_shouldAssignTrackIds() {
        final AlbumImporter.ImportConfig config = AlbumImporter.ImportConfig.defaults().path(importPath);
        final Album album = albumImporter.importAlbumFromPath(config);
        assertThat(album.getTracks()).extracting(t -> t.getId()).containsExactly("123", "456");
    }

    @Test
    public void testImportWithDefaultConfig_shouldFormatTrackNums() {
        final AlbumImporter.ImportConfig config = AlbumImporter.ImportConfig.defaults().path(importPath);
        final Album album = albumImporter.importAlbumFromPath(config);
        assertThat(album.getTracks()).extracting(t -> t.getTrackNum()).containsExactly("1", "2");
    }

    @Test
    public void testImportWith10Tracks_shouldSortAndPadZerosToTrackNums() {
        final AlbumImporter.ImportConfig config = AlbumImporter.ImportConfig.defaults().path(importPath);
        final List<Track> tracks = IntStream.range(1, 11)
                .mapToObj(i -> new Track().setId("" + i).setTrackNum("" + i))
                .collect(Collectors.toList());

        Collections.shuffle(tracks, new Random(1));

        when(mp3Reader.readDir(any(Path.class), any(Mp3Reader.Config.class))).thenReturn(tracks);

        final Album album = albumImporter.importAlbumFromPath(config);
        assertThat(album.getTracks()).extracting(t -> t.getTrackNum())
                .containsExactly("01", "02", "03", "04", "05", "06", "07", "08", "09", "10");
    }

    @Test
    public void testImportWithOverwriteName_shouldReturnAlbumWithOverwrittenName() {
        final AlbumImporter.ImportConfig config =
                AlbumImporter.ImportConfig.defaults().path(importPath).albumName("New Hits");
        final Album album = albumImporter.importAlbumFromPath(config);

        assertThat(album.getId()).isEqualTo("AAA");
        assertThat(album.getTracks()).containsExactly(track1, track2);
        assertThat(album.getName()).isEqualTo("New Hits");
        assertThat(album.getYear()).isEqualTo(2021);
        assertThat(album.getArtists()).containsExactlyInAnyOrder("Anonymous", "Le Vamp");
    }

    @Test
    public void testImportWithOverwriteYear_shouldReturnAlbumWithOverwrittenYear() {
        final AlbumImporter.ImportConfig config =
                AlbumImporter.ImportConfig.defaults().path(importPath).albumYear(2000);
        final Album album = albumImporter.importAlbumFromPath(config);

        assertThat(album.getId()).isEqualTo("AAA");
        assertThat(album.getTracks()).containsExactly(track1, track2);
        assertThat(album.getName()).isEqualTo("Aretha Franklin");
        assertThat(album.getYear()).isEqualTo(2000);
        assertThat(album.getArtists()).containsExactlyInAnyOrder("Anonymous", "Le Vamp");
    }

    @Test
    public void testImportWithCopyFiles_shouldCopyAlbumCover() {
        final AlbumImporter.ImportConfig config =
                AlbumImporter.ImportConfig.defaults().path(importPath).copyFiles(true);
        final Album album = albumImporter.importAlbumFromPath(config);

        assertThat(album.getId()).isEqualTo("AAA");
        assertThat(album.getImageUrl()).isEqualTo(albumCoverUrl);
        assertThat(track1.getImageUrl()).isEqualTo(albumCoverUrl);
        assertThat(track2.getImageUrl()).isEqualTo(albumCoverUrl);
    }

    @Test
    public void testImportWithCopyFiles_shouldSetTracksNewSourcePath() {
        final AlbumImporter.ImportConfig config =
                AlbumImporter.ImportConfig.defaults().path(importPath).copyFiles(true);
        final Album album = albumImporter.importAlbumFromPath(config);

        assertThat(album.getId()).isEqualTo("AAA");
        assertThat(track1.getSourcePath()).isEqualTo(Path.of("src/test/dest/123.mp3"));
        assertThat(track2.getSourcePath()).isEqualTo(Path.of("src/test/dest/456.mp3"));
    }
}