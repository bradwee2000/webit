package com.bwee.webit.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MusicUtils {
    private static final Pattern SPLIT_ARTIST_DELIMITER = Pattern.compile("[&,;/]");
    private static final String JOIN_ARTIST_SEPARATOR = ", ";

    public static String padZeros(final Integer num, int digits) {
        if (num == null) {
            return null;
        }
        return String.format("%0" + digits + "d", num);
    }

    public static String padZeros(final String num, int digits) {
        return padZeros(Integer.parseInt(num), digits);
    }

    public static boolean isMp3(Path path) {
        try {
            return StringUtils.equals("audio/mpeg", Files.probeContentType(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isImage(Path path) {
        try {
            final String contentType = Files.probeContentType(path);
            return contentType != null && contentType.startsWith("image/jpeg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Path> listFiles(Path path) {
        try {
            return Files.list(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> splitArtist(final String artist) {
        if (artist == null) {
            return Collections.emptyList();
        }
        return Splitter.on(SPLIT_ARTIST_DELIMITER)
                .trimResults()
                .omitEmptyStrings()
                .splitToList(artist)
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public static String joinArtistsToString(final Collection<String> artists) {
        return Joiner.on(JOIN_ARTIST_SEPARATOR).join(artists);
    }
}
