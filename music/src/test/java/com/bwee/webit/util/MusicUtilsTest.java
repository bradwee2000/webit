package com.bwee.webit.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class MusicUtilsTest {

    @Test
    public void testPadZeros_shouldPadZeros() {
        assertThat(MusicUtils.padZeros("00010", 1)).isEqualTo("10");
        assertThat(MusicUtils.padZeros("00010", 2)).isEqualTo("10");
        assertThat(MusicUtils.padZeros("00010", 3)).isEqualTo("010");
        assertThat(MusicUtils.padZeros("00010", 9)).isEqualTo("000000010");
    }

    @Test
    public void testSplitArtist_shouldSplitArtistByDelimiters() {
        assertThat(MusicUtils.splitArtist("Aron  & Jack Black")).containsExactly("Aron", "Jack Black");
        assertThat(MusicUtils.splitArtist("Aron ,, Jack Black")).containsExactly("Aron", "Jack Black");
        assertThat(MusicUtils.splitArtist("Aron / Jack Black")).containsExactly("Aron", "Jack Black");
        assertThat(MusicUtils.splitArtist("Jack Black")).containsExactly("Jack Black");
        assertThat(MusicUtils.splitArtist("")).isEmpty();
        assertThat(MusicUtils.splitArtist(null)).isEmpty();
    }

    @Test
    public void testJoinArtistsToString_shouldReturnCommaSeparatedString() {
        assertThat(MusicUtils.joinArtistsToString(Arrays.asList("Jon", "Beck Lee", "Dim Wit")))
                .isEqualTo("Jon, Beck Lee, Dim Wit");
    }
}