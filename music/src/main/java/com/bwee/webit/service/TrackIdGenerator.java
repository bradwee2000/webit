package com.bwee.webit.service;

import com.bwee.webit.core.IdGenerator;
import com.bwee.webit.model.Track;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.util.Base64;

import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.upperCase;

@Slf4j
public class TrackIdGenerator implements IdGenerator<Track> {

    private final MessageDigest md;

    public TrackIdGenerator(final MessageDigest md) {
        this.md = md;
    }

    @SneakyThrows
    public String generateId(final Track track) {
        final String musicValues = lowerCase(new StringBuilder()
                .append(track.getTitle())
                .append(track.getAlbumName())
                .append(track.getArtist())
                .append(track.getExt())
                .toString());

        final byte[] hash = md.digest(musicValues.getBytes());
        final String id = new String(Base64.getUrlEncoder().encode(hash));
        return id;
    }
}
