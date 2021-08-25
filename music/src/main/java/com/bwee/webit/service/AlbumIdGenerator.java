package com.bwee.webit.service;

import com.bwee.webit.model.Album;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Base64;

@Slf4j
public class AlbumIdGenerator implements IdGenerator<Album> {

    private final MessageDigest md;

    public AlbumIdGenerator(final MessageDigest md) {
        this.md = md;
    }

    @SneakyThrows
    public String generateId(final Album album) {
        final Path sourcePath = Path.of(album.getSourcePath());
        final String filename = sourcePath.getFileName().toString();
        final byte[] hash = md.digest(filename.getBytes());
        return new String(Base64.getUrlEncoder().encode(hash));
    }
}
