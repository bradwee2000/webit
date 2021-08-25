package com.bwee.webit.service.strategy.cover;

import com.bwee.webit.util.MusicUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

@Slf4j
@Order(1)
@Component
public class SimpleAlbumCoverProvider implements AlbumCoverProvider {

    private static final Function<Path, Integer> SCORE_FUNCTION = (path) -> {
        final String filename = path.getFileName().toString();
        if (containsIgnoreCase(filename, "front")) {
            return 10;
        }
        if (containsIgnoreCase(filename, "cover")) {
            return 9;
        }
        if (containsIgnoreCase(filename, "folder")) {
            return 8;
        }
        return 1;
    };

    @Override
    public Optional<byte[]> provide(Path albumPath) {
        try {
            final List<Path> imagePaths = Files.walk(albumPath)
                    .filter(p -> MusicUtils.isImage(p))
                    .sorted(Comparator.comparing(SCORE_FUNCTION).reversed())
                    .collect(Collectors.toList());

            return imagePaths.stream()
                    .findFirst()
                    .map(p -> readAllBytes(p));

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }



    private byte[] readAllBytes(Path path) {
        try {
            log.info("Copying Image: {}", path.toString());
            return Files.readAllBytes(path);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
