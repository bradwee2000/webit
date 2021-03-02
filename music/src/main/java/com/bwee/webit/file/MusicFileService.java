package com.bwee.webit.file;


import com.bwee.webit.model.Track;
import com.bwee.webit.core.FileService;

import java.nio.file.Path;

public class MusicFileService {

    private final FileService fileService;
    private final String musicStorePath;

    public MusicFileService(final FileService fileService, final String musicStorePath) {
        this.fileService = fileService;
        this.musicStorePath = musicStorePath;
    }

    public Path put(final Path sourcePath, final Track track) {
        final Path destPath = toStoragePath(track);
        fileService.copy(sourcePath, destPath);
        return destPath;
    }

    public Path toStoragePath(final Track track) {
        return Path.of(musicStorePath, track.getAlbumName(), toFilename(track));
    }

    private String toFilename(final Track track) {
        return track.getArtist() + " - " + track.getTitle() + "." + track.getExt();
    }
}
