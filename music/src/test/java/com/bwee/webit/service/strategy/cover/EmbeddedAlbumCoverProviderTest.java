package com.bwee.webit.service.strategy.cover;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class EmbeddedAlbumCoverProviderTest {

    private final Path albumPath = Path.of(new File("src/test/resources/albums/Aretha Franklin").toURI());
    private EmbeddedAlbumCoverProvider provider = new EmbeddedAlbumCoverProvider();

    @Test
    public void testProvide_shouldReturnImageBytes() throws IOException {
        final Optional<byte[]> imageBytes = provider.provide(albumPath);
        assertThat(imageBytes).isPresent();
        assertThat(imageBytes.get().length).isEqualTo(38620);
    }
}