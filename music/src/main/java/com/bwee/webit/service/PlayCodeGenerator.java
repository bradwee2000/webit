package com.bwee.webit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
public class PlayCodeGenerator {

    private final MessageDigest md;
    private final Clock clock;
    private final int changeIntervalSec;

    public PlayCodeGenerator(final MessageDigest md, final Clock clock, int changeIntervalSec) {
        this.md = md;
        this.clock = clock;
        this.changeIntervalSec = changeIntervalSec;
    }

    public String generateCode() {
        return generateCode(clock.millis());
    }

    public String previousCode() {
        return generateCode(clock.instant().minusSeconds(changeIntervalSec).toEpochMilli());
    }

    private String generateCode(final long millis) {
        final long secondsNow = (millis / 1000 / changeIntervalSec) * changeIntervalSec; // round down to nearest interval
        final byte[] hash = md.digest(String.valueOf(secondsNow).getBytes());
        return new String(Base64.getUrlEncoder().encode(hash));
    }
}
