package com.bwee.webit.service.strategy.cover;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class SimpleAlbumCoverProviderTest {

    private final Path albumPath = Path.of(new File("src/test/resources/albums/Aretha Franklin").toURI());
    private SimpleAlbumCoverProvider provider = new SimpleAlbumCoverProvider();

    @Test
    public void testProvide_shouldReturnImageBytes() {
        final Optional<byte[]> imageBytes = provider.provide(albumPath);
        assertThat(imageBytes).isPresent();
        assertThat(imageBytes.get().length).isEqualTo(188736);
    }

    @Test
    public void testProvideWithMultipleImages_shouldReturnFirstInPriority() {
        final Path coverPath = Path.of(new File("src/test/resources/albums/cover").toURI());
        final Optional<byte[]> imageBytes = provider.provide(coverPath);
        assertThat(imageBytes).isPresent();
        assertThat(new String(imageBytes.get())).isEqualTo("Front Image");
    }
}