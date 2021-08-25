package com.bwee.webit.file;

import com.bwee.webit.model.Track;
import com.bwee.webit.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MusicFileServiceTest {

    private MusicFileService musicFileService;
    private FileService fileService;

    @BeforeEach
    public void before() {
        fileService = mock(FileService.class);
        musicFileService = new MusicFileService(fileService, "/media");
    }

    @Test
    public void testPut_shouldGenerateFilename() {
        final Track track = new Track()
                .setTrackNum("01")
                .setAlbumName("Sample Album")
                .setOriginalAlbumName("Sample_Album")
                .setAlbumId("ABC")
                .setArtists(Arrays.asList("Leo", "Brian McKnight"))
                .setExt("mp3")
                .setTitle("Sample Track");

        musicFileService.put(Path.of("/path/of/source"), track);

        final String expectedFilename = "01. Leo, Brian McKnight - Sample Track.mp3";

        verify(fileService).copy(
                Path.of("/path/of/source"),
                Path.of("/media", "Sample_Album", "ABC", expectedFilename));
    }

    @Test
    public void testPutWithMultipleArtists_shouldLimitArtists() {
        final Track track = new Track()
                .setTrackNum("01")
                .setAlbumName("Sample Album")
                .setOriginalAlbumName("Sample_Album")
                .setAlbumId("ABC")
                .setArtists(Arrays.asList("Leo", "Brian McKnight", "Cher", "Amanda", "Reggie", "Veronica", "Betty"))
                .setExt("mp3")
                .setTitle("Sample Track");

        musicFileService.put(Path.of("/path/of/source"), track);

        final String expectedFilename = "01. Leo, Brian McKnight, Cher, Amanda, Reggie, Veronica - Sample Track.mp3";

        verify(fileService).copy(
                Path.of("/path/of/source"),
                Path.of("/media", "Sample_Album", "ABC", expectedFilename));
    }

}