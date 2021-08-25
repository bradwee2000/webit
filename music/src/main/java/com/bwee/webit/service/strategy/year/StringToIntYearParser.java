package com.bwee.webit.service.strategy.year;

public class StringToIntYearParser implements YearParser {

    public Integer parse(String year) {
        return Integer.parseInt(year);
    }
}
