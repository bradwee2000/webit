package com.bwee.webit.service;

import com.bwee.webit.model.Album;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.util.Base64;

import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.upperCase;

@Slf4j
public class AlbumIdGenerator implements IdGenerator<Album> {

    private final MessageDigest md;

    public AlbumIdGenerator(final MessageDigest md) {
        this.md = md;
    }

    @SneakyThrows
    public String generateId(final Album album) {
        final String musicValues = lowerCase(new StringBuilder()
                .append(album.getName())
                .append(album.getYear())
                .toString());

        final byte[] hash = md.digest(musicValues.getBytes());
        return new String(Base64.getUrlEncoder().encode(hash));
    }
}
