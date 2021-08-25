package com.bwee.webit.file;


import com.bwee.webit.model.Track;
import com.bwee.webit.service.FileService;
import com.google.common.base.Joiner;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

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
        return Path.of(musicStorePath, track.getOriginalAlbumName(), track.getAlbumId(), toFilename(track));
    }

    private String toFilename(final Track track) {
        // Limit to 6 artists
        final String artists = Joiner.on(", ").join(track.getArtists().stream()
                .limit(6)
                .collect(Collectors.toList()));

        return new StringBuilder()
                .append(track.getTrackNum()).append(". ")
                .append(artists).append(" - ")
                .append(track.getTitle()).append(".")
                .append(track.getExt())
                .toString();
    }
}
