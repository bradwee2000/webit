package com.bwee.webit.service;

import com.bwee.webit.file.MusicFileService;
import com.bwee.webit.model.Track;
import com.bwee.webit.image.ImageService;
import com.bwee.webit.model.Album;
import com.bwee.webit.util.ImportUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.bwee.webit.util.ImportUtils.padZeros;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class AlbumImporter {

    @Autowired
    private TrackService trackService;

    @Autowired
    private AlbumService albumService;

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

    @SneakyThrows
    public Album importAlbumFromPath(final ImportConfig importConfig) {
        log.info("Import music from: path={}, config={}", importConfig.path(), importConfig);

        final Mp3Reader.Config config = Mp3Reader.Config.defaults()
                .tags(importConfig.albumTags())
                .albumName(importConfig.albumName())
                .useFileNameAsTrackNum(importConfig.useFilenameAsTrackNum());

        final List<Track> importedTrackList = mp3Reader.readDir(importConfig.path(), config);

        final List<String> artists = importedTrackList.stream()
                .map(m -> m.getArtist())
                .filter(artist -> !StringUtils.isEmpty(artist))
                .distinct()
                .collect(toList());

        final String finalAlbumName = StringUtils.isEmpty(config.albumName()) ?
                importedTrackList.stream().findFirst().map(t -> t.getAlbumName()).orElse(null) :
                config.albumName();
        final int finalYear = importConfig.albumYear() == null ?
                importedTrackList.stream().findFirst().map(t -> t.getYear()).orElse(0) :
                importConfig.albumYear();

        final Album album = new Album()
                .setName(finalAlbumName)
                .setArtists(artists)
                .setTags(importConfig.albumTags())
                .setYear(finalYear);

        // Generate album ID based on its unique fields
        album.setId(albumIdGenerator.generateId(album));

        // Assign album details to music and generate ID based on its unique fields
        final int numOfDigits = String.valueOf(importedTrackList.size()).length();
        final List<Track> trackList = importedTrackList.stream()
                .map(m -> m.setAlbumId(album.getId()))
                .map(m -> m.setAlbumName(finalAlbumName))
                .map(m -> m.setId(trackIdGenerator.generateId(m)))
                .map(m -> m.setTrackNum(ImportUtils.padZeros(m.getTrackNum(), numOfDigits)))
                .sorted(Comparator.comparing(Track::getTrackNum))
                .collect(toList());

        album.setTracks(trackList);

        if (importConfig.copyFiles()) {
            final String albumCoverUrl = copyAlbumCover(importConfig) ;
            album.setImageUrl(albumCoverUrl);
            trackList.forEach(track -> track.setImageUrl(albumCoverUrl));
            copyTracks(trackList);
        }

        trackService.saveAll(trackList);
        albumService.save(album);

        log.info("Saved album: {}", album);
        return album;
    }

    @SneakyThrows
    private String copyAlbumCover(final ImportConfig importConfig) {
        // Store and set album image
        final Mp3Reader.AlbumCover albumCover = mp3Reader.albumCover(importConfig.path());
        final String imageUrl = imageService.put("music", Files.readAllBytes(albumCover.getPath()));
        return imageUrl;
    }

    @SneakyThrows
    private void copyTracks(final List<Track> tracks) {
        for (final Track track : tracks) {
            final Path destPath = musicFileService.put(track.getSourcePath(), track);
            track.setSourcePath(destPath);
            log.info("Copied from {} to {}", track.getSourcePath(), destPath);
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
        private boolean useFilenameAsTrackNum;
        private boolean copyFiles;
    }
}
