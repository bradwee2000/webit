package com.bwee.webit.service;

import com.bwee.webit.core.FileService;
import com.bwee.webit.file.MusicFileService;
import com.bwee.webit.image.ImageService;
import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class MusicAlbumImporter {

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

    @Value("${music.import.path:~/}")
    private String musicDestPath;

    @SneakyThrows
    public Album importAlbumFromPath(final Path path,
                                     final String albumName,
                                     final List<String> albumTags,
                                     final int albumYear,
                                     final boolean useFilenameAsTrackNum,
                                     final boolean copyFiles) {
        final Mp3Reader.Config config = Mp3Reader.Config.defaults()
                .tags(albumTags)
                .albumName(albumName)
                .useFileNameAsTrackNum(useFilenameAsTrackNum);

        log.info("Import music from: path={}, config={}", path, config);

        final List<Track> importedTrackList = mp3Reader.readDir(path.toAbsolutePath().toString(), config);

        final List<String> artists = importedTrackList.stream()
                .map(m -> m.getArtist())
                .filter(artist -> !StringUtils.isEmpty(artist))
                .distinct()
                .collect(toList());

        final Album album = new Album()
                .setName(albumName)
                .setArtists(artists)
                .setTags(albumTags)
                .setYear(albumYear);

        // Generate album ID based on its unique fields
        album.setId(albumIdGenerator.generateId(album));

        // Assign album details to music and generate ID based on its unique fields
        final List<Track> trackList = importedTrackList.stream()
                .map(m -> m.setAlbumId(album.getId()).setAlbumName(albumName))
                .map(m -> m.setId(trackIdGenerator.generateId(m)))
                .collect(toList());

        album.setTracks(trackList);

        if (copyFiles) {
            // Store and set album image
            final Mp3Reader.AlbumCover albumCover = mp3Reader.albumCover(path);
            final String imageUrl = imageService.put("music", Files.readAllBytes(albumCover.getPath()));
            album.setImageUrl(imageUrl);

            for (final Track track : trackList) {
                track.setImageUrl(imageUrl);
                final Path destPath = musicFileService.put(track.getSourcePath(), track);
                track.setSourcePath(destPath);
                log.info("Copied from {} to {}", track.getSourcePath(), destPath);
            }
        }

        trackService.saveAll(trackList);
        albumService.save(album);

        log.info("Saved album: {}", album);
        return album;
    }
}
