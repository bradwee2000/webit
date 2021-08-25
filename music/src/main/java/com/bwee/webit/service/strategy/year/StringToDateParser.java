package com.bwee.webit.service.strategy.year;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
public class StringToDateParser implements YearParser {

    public Integer parse(String year) {
        try {
            final LocalDateTime time = LocalDateTime.ofInstant(Instant.parse(year), ZoneOffset.UTC);
            return time.getYear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
