package com.bwee.webit.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImportUtilsTest {

    @Test
    public void testPadZeros_shouldPadZeros() {
        assertThat(ImportUtils.padZeros("00010", 1)).isEqualTo("10");
        assertThat(ImportUtils.padZeros("00010", 2)).isEqualTo("10");
        assertThat(ImportUtils.padZeros("00010", 3)).isEqualTo("010");
        assertThat(ImportUtils.padZeros("00010", 9)).isEqualTo("000000010");
    }
}