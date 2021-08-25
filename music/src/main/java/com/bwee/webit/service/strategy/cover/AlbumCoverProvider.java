package com.bwee.webit.service.strategy.cover;

import java.nio.file.Path;
import java.util.Optional;

public interface AlbumCoverProvider {

    Optional<byte[]> provide(Path albumPath);
}
