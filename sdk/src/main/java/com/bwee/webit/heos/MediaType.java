package com.bwee.webit.heos;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
public enum MediaType {

    SONG("song"),
    STATION("station"),
    GENRE("gendre"),
    ARTIST("artist"),
    ALBUM("album"),
    CONTAINER("container");

    public static MediaType of(final String code) {
        return Arrays.stream(MediaType.values())
                .filter(m -> StringUtils.equals(code, m.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("MediaType not found: " + code));
    }

    String code;

    MediaType(String code) {
        this.code = code;
    }
}
