package com.bwee.webit.service.strategy.year;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
public class MultiYearParser implements YearParser {

    private final List<YearParser> parsers;

    public MultiYearParser(final List<YearParser> parsers) {
        this.parsers = parsers;
    }

    public Integer parse(final String year) {
        for (YearParser parser : parsers) {
            try {
                return parser.parse(year);
            } catch (final Exception e) {
                // do nothing
            }
        }
        log.warn("Unable to parse year: {}", year);
        return null;
    }
}
