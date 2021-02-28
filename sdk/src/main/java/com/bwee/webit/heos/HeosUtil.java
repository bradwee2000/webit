package com.bwee.webit.heos;

import com.google.common.base.Splitter;
import com.google.common.collect.Streams;

import java.util.Map;
import java.util.stream.Collectors;

public class HeosUtil {

    public static Map<String, String> toMap(final String message) {
        return Streams.stream(Splitter.on('&').split(message))
                .map(param -> param.split("="))
                .collect(Collectors.toUnmodifiableMap(e -> e[0], e -> e[1]));
    }
}
