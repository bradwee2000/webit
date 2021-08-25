package com.bwee.webit.service.strategy.year;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@Component
class StringToIntYearParserTest {

    private final StringToIntYearParser parser = new StringToIntYearParser();

    @Test
    public void testParse_shouldReturnIntValue() {
        assertThat(parser.parse("2012")).isEqualTo(2012);
    }

}