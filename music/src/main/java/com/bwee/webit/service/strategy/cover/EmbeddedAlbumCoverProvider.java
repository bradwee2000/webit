package com.bwee.webit.service.strategy.cover;

import com.bwee.webit.exception.InvalidMp3Exception;
import com.mpatric.mp3agic.Mp3File;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static com.bwee.webit.util.MusicUtils.isMp3;
import static com.bwee.webit.util.MusicUtils.listFiles;

@Order(2)
@Slf4j
@Component
public class EmbeddedAlbumCoverProvider implements AlbumCoverProvider {

    @SneakyThrows
    @Override
    public Optional<byte[]> provide(final Path albumPath) {
        return Files.walk(albumPath)
                .filter(p -> isMp3(p))
                .findAny()
                .map(this::toMp3File)
                .map(mp3 -> mp3.getId3v2Tag())
                .map(id3v2 -> id3v2.getAlbumImage());
    }

    private Mp3File toMp3File(final Path path) {
        try {
            return new Mp3File(path.toAbsolutePath());
        } catch (final Exception e) {
            throw new InvalidMp3Exception(path.getFileName().toString(), e.getMessage());
        }
    }
}
