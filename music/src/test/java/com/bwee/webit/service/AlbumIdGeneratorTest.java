package com.bwee.webit.service;

import com.bwee.webit.model.Album;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class AlbumIdGeneratorTest {

    private AlbumIdGenerator albumIdGenerator;

    @BeforeEach
    private void before() throws NoSuchAlgorithmException {
        albumIdGenerator = new AlbumIdGenerator(MessageDigest.getInstance("SHA-1"));
    }

    @Test
    public void testGenerateIdForSameDirectory_shouldReturnEqualId() {
        final String id1 = albumIdGenerator.generateId(new Album().setSourcePath("/music/sample"));
        final String id2 = albumIdGenerator.generateId(new Album().setSourcePath("/music/sample"));
        assertThat(id1).isEqualTo(id2);
    }

    @Test
    public void testGenerateIdForSameDirectoryName_shouldReturnEqualId() {
        final String id1 = albumIdGenerator.generateId(new Album().setSourcePath("/music/sample"));
        final String id2 = albumIdGenerator.generateId(new Album().setSourcePath("/music/test/sample"));
        assertThat(id1).isEqualTo(id2);
    }

    @Test
    public void testGenerateIdForDifferentDirectoryName_shouldReturnUniqueId() {
        final String id1 = albumIdGenerator.generateId(new Album().setSourcePath("/music/sample1"));
        final String id2 = albumIdGenerator.generateId(new Album().setSourcePath("/music/sample2"));
        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    public void test() {
        //r5o7L723jn0w3EsaPO9mKC19kp0
        String id = albumIdGenerator.generateId(new Album().setSourcePath("VA - Piano for Valentine's Day (2021) Mp3 320kbps [PMEDIA] ⭐️"));
        log.info("ID: {}", id);
    }
}