package com.bwee.webit.service.strategy.year;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringToDateParserTest {

    private final StringToDateParser parser = new StringToDateParser();

    @Test
    public void testParse_shouldReturnYear() {
        assertThat(parser.parse("2013-01-04T08:00:00Z")).isEqualTo(2013);
    }
}