package com.bwee.webit.service;

import com.bwee.webit.file.MusicFileService;
import com.bwee.webit.model.Track;
import com.bwee.webit.image.ImageService;
import com.bwee.webit.model.Album;
import com.bwee.webit.service.strategy.cover.AlbumCoverProvider;
import com.bwee.webit.util.MusicUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.bwee.webit.util.MusicUtils.padZeros;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class AlbumImporter {

    @Autowired
    private TrackService trackService;

    @Autowired
    private Mp3Reader mp3Reader;

    @Autowired
    private FileService fileService;

    @Autowired
    private AlbumIdGenerator albumIdGenerator;

    @Autowired
    private TrackIdGenerator trackIdGenerator;

    @Autowired
    private ImageService imageService;

    @Autowired
    private MusicFileService musicFileService;

    @Autowired
    private List<AlbumCoverProvider> albumCoverProviders;

    @Value("${music.storage.unprocessed.path:~/Downloads/Music}")
    private String unprocessedMusicPath;

    @Autowired
    private AlbumImportCacheService albumCacheService;

    public List<Path> listUnprocessedMusicPaths() {
        return listUnprocessedMusicPaths(null);
    }

    public List<Path> listUnprocessedMusicPaths(final String subPath) {
        try {
            final Path path = Optional.ofNullable(subPath)
                    .map(s -> Path.of(unprocessedMusicPath, s))
                    .orElse(Path.of(unprocessedMusicPath));
            return Files.list(path)
                    .sorted(comparing(Path::getFileName))
                    .collect(toList());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Album> importAllAlbums(final Integer limit) {
        return listUnprocessedMusicPaths().stream()
                .map(path -> AlbumImporter.ImportConfig.defaults().path(path))
                .map(config -> {
                    try {
                        return importAlbumFromPath(config);
                    } catch (Exception e) {
                        log.error("Failed to load album from " + config.path, e);
                        return null;
                    }
                })
                .filter(importedAlbum -> importedAlbum != null)
                .filter(importedAlbum -> !importedAlbum.isExisting())
                .map(importedAlbum -> importedAlbum.getAlbum())
                .filter(album -> !album.getTracks().isEmpty())
                .limit(limit)
                .collect(toList());
    }

    @SneakyThrows
    public ImportedAlbum importAlbumFromPath(final ImportConfig importConfig) {
        log.info("Import music from: path={}", importConfig.path());

        final String albumId = albumIdGenerator.generateId(new Album().setSourcePath(importConfig.path().toAbsolutePath().toString()));
        final Optional<Album> existingAlbum = albumCacheService.get(albumId);

        // Ignore if album is existing
        if (!importConfig.overwriteExisting() && existingAlbum.isPresent()) {
            log.info("Album exists. Skipping: {}", importConfig.path());
            return new ImportedAlbum().setAlbum(existingAlbum.get()).setExisting(true);
        }

        final Mp3Reader.Config config = Mp3Reader.Config.defaults()
                .tags(importConfig.albumTags())
                .albumName(importConfig.albumName())
                .useFileNameAsTrackNum(importConfig.useFilenameAsTrackNum());

        final List<Track> importedTrackList = mp3Reader.readDir(importConfig.path(), config);

        final List<String> artists = extractArtists(importedTrackList);

        final String originalAlbumName = importedTrackList.stream()
                .findFirst()
                .map(t -> t.getAlbumName())
                .orElse(null);
        final String displayAlbumName = StringUtils.isEmpty(config.albumName()) ?
                originalAlbumName :
                config.albumName();

        final Integer finalYear = importConfig.albumYear() == null ?
                importedTrackList.stream().findFirst().map(t -> t.getYear()).orElse(null) :
                importConfig.albumYear();

        final Album album = new Album()
                .setId(albumId)
                .setOriginalName(originalAlbumName)
                .setDisplayName(displayAlbumName)
                .setArtists(artists)
                .setTags(importConfig.albumTags())
                .setYear(finalYear)
                .setSourcePath(importConfig.path.toAbsolutePath().toString());

        // Assign album details to music and generate ID based on its unique fields
        final int numOfDigits = String.valueOf(importedTrackList.size()).length();
        final List<Track> trackList = importedTrackList.stream()
                .map(m -> m.setAlbumId(album.getId())
                        .setAlbumName(displayAlbumName)
                        .setOriginalAlbumName(originalAlbumName)
                        .setYear(finalYear)
                        .setTrackNum(MusicUtils.padZeros(m.getTrackNum(), numOfDigits))
                        .setId(trackIdGenerator.generateId(m))
                )
                .sorted(comparing(Track::getTrackNum))
                .collect(toList());

        album.setTracks(trackList);

        if (importConfig.copyFiles()) {
            final String albumCoverUrl = copyAlbumCover(importConfig) ;
            album.setImageUrl(albumCoverUrl);
            trackList.forEach(track -> track.setImageUrl(albumCoverUrl));
            copyTracks(trackList);
        }

        trackService.saveAll(trackList);
        albumCacheService.put(album);

        log.info("Saved album: {}", album.getDisplayName());
        return new ImportedAlbum().setAlbum(album).setExisting(existingAlbum.isPresent());
    }

    private List<String> extractArtists(final Collection<Track> tracks) {
        return tracks.stream()
                .flatMap(m -> m.getArtists().stream())
                .filter(artist -> !StringUtils.isEmpty(artist))
                .distinct()
                .collect(toList());
    }

    @SneakyThrows
    private String copyAlbumCover(final ImportConfig importConfig) {
        byte[] albumCoverBytes = albumCoverProviders.stream()
                .map(p -> p.provide(importConfig.path))
                .filter(bytes -> bytes.isPresent())
                .map(bytes -> bytes.get())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No album cover found!"));

        final String imageUrl = imageService.put("music", albumCoverBytes);
        return imageUrl;
    }

    @SneakyThrows
    private void copyTracks(final List<Track> tracks) {
        for (final Track track : tracks) {
            final Path destPath = musicFileService.put(track.getSourcePath(), track);
            track.setSourcePath(destPath);
            log.info("Copied {}", destPath);
        }
    }

    @Data
    @Accessors(fluent = true)
    public static class ImportConfig {
        public static ImportConfig defaults() {
            return new ImportConfig();
        }

        private Path path;
        private String albumName;
        private List<String> albumTags = Collections.emptyList();
        private Integer albumYear;
        private boolean useFilenameAsTrackNum = true;
        private boolean copyFiles = true;
        private boolean overwriteExisting = false;
    }

    @Data
    @Accessors(chain = true)
    public static class ImportedAlbum {
        private boolean isExisting;
        private Album album;
    }
}
