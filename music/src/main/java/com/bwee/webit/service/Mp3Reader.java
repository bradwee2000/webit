package com.bwee.webit.service;

import com.bwee.webit.exception.ID3V2NotFoundException;
import com.bwee.webit.exception.InvalidMp3Exception;
import com.bwee.webit.model.Genre;
import com.bwee.webit.model.Track;
import com.bwee.webit.util.ImportUtils;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.bwee.webit.util.ImportUtils.padZeros;
import static java.nio.file.Files.isDirectory;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Slf4j
public class Mp3Reader {

    public Track read(final String filePath) {
        return read(Path.of(filePath));
    }

    @SneakyThrows
    public Track read(Path path) {
        final Mp3File file;

        try {
            file = new Mp3File(path.toAbsolutePath());
        } catch (final InvalidDataException e) {
            throw new InvalidMp3Exception(path.getFileName().toString(), e.getMessage());
        }
//        log.info("Path: {}", path);
//        log.info("Title: {}", file.getId3v2Tag().getTitle());
//        log.info("Artist: {}", file.getId3v2Tag().getArtist());
//        log.info("Album Artist: {}", file.getId3v2Tag().getAlbumArtist());
//        log.info("Genre: {}", file.getId3v2Tag().getGenre());
//        log.info("Track: {}", file.getId3v2Tag().getTrack());
//        log.info("Album: {}", file.getId3v2Tag().getAlbum());
//        log.info("Composer: {}", file.getId3v2Tag().getComposer());
//        log.info("Lyrics: {}", file.getId3v2Tag().getLyrics());
//        log.info("Length: {}", file.getId3v2Tag().getDataLength());
//        log.info("Date: {}", file.getId3v2Tag().getDate());
//        log.info("Encoder: {}", file.getId3v2Tag().getEncoder());
//        log.info("Original Artist: {}", file.getId3v2Tag().getOriginalArtist());
//        log.info("Year: {}", file.getId3v2Tag().getYear());
//        log.info("Bit Rate: {}", file.getBitrate());
//        log.info("Channel: {}", file.getChannelMode());
//        log.info("Offset: {} to {}", file.getStartOffset(), file.getEndOffset());
//        log.info("DURATION: {}", file.getLengthInSeconds());
//        log.info("Sample Rate: {}", file.getSampleRate());
//        log.info("Frame Count: {}", file.getFrameCount());
//        log.info("Size: {}", file.getLength());

        final ID3v2 id3 = Optional.ofNullable(file.getId3v2Tag()).orElseThrow(() -> new ID3V2NotFoundException(file.getFilename()));

        final Genre genre = Genre.of(id3.getGenre());
        final String genreDescription = genre == Genre.UNKNOWN ? id3.getGenreDescription() : genre.getName();
        final Integer year = Optional.ofNullable(id3.getYear()).map(Integer::parseInt).orElse(null);
        final String trackNum = Optional.ofNullable(id3.getTrack())
                .map(t -> ImportUtils.padZeros(getTrack(id3.getTrack()), 9))
                .orElse(null);

        final Track track = new Track()
                .setTitle(formatName(id3.getTitle()))
                .setArtist(id3.getArtist())
                .setAlbumName(id3.getAlbum())
                .setYear(year)
                .setComposer(id3.getComposer())
                .setGenre(genreDescription == null ? emptyList() : singletonList(genreDescription))
                .setBitRate(file.getBitrate())
                .setTrackNum(trackNum)
                .setDurationMillis(file.getLengthInMilliseconds())
                .setSampleRate(file.getSampleRate())
                .setOriginalArtist(id3.getOriginalArtist())
                .setSize(file.getLength())
                .setChannel(WordUtils.capitalizeFully(file.getChannelMode()))
                .setSourcePath(path)
                .setExt("mp3");

        return track;
    }

    public String formatName(final String name) {
        if (name == null) {
            return null;
        }

        return StringUtils.replaceChars(name, "/", "-");
    }

    @SneakyThrows
    public List<Track> readDir(String musicPath) {
        return readDir(musicPath, Config.defaults());
    }

    public List<Track> readDir(final String mp3Path, final Config config) {
        return readDir(Path.of(mp3Path), config);
    }

    @SneakyThrows
    public List<Track> readDir(final Path path, final Config config) {
        return readDir(path, config, new AtomicInteger(1));
    }

    @SneakyThrows
    public List<Track> readDir(final Path path, final Config config, final AtomicInteger trackCounter) {
        if (!isDirectory(path)) {
            return singletonList(read(path));
        }

        // Process all music from directory
        final List<Track> dirMusic = readMusicFromPath(path, config, trackCounter);

        // Process all music from sub-directories
        final List<Track> subDirTracks = Files.list(path)
                .filter(p -> isDirectory(p))
                .sorted()
                .map(p -> readDir(p, config, trackCounter))
                .flatMap(music -> music.stream())
                .collect(toList());

        // Return all music
        final List<Track> allTrack = Stream.concat(dirMusic.stream(), subDirTracks.stream())
                .sorted(comparing(m -> m.getTrackNum()))
                .collect(toList());
        return allTrack;
    }

    @SneakyThrows
    private List<Track> readMusicFromPath(final Path path, final Config config, final AtomicInteger trackCounter) {
        Stream<Track> musicStream = Files.list(path)
                .filter(p -> isMp3(p))
                .map(p -> read(p).setTags(config.tags()));

            musicStream = musicStream.sorted(comparing(music -> music.getSourcePath().getFileName().toString()))
                    .map(music -> music.setTrackNum(ImportUtils.padZeros(trackCounter.getAndIncrement(), 9)));

        return musicStream.collect(toList());
    }

    private boolean isMp3(Path path) {
        try {
            return StringUtils.equals("audio/mpeg", Files.probeContentType(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public AlbumCover albumCover(Path path) {
        final Path coverPath = Files.walk(path)
            .filter(p -> isImage(p))
            .findFirst().orElseThrow(() -> new IllegalStateException("No album cover found!"));

//        final byte[] imageBytes = Files.readAllBytes(coverPath);

        return new AlbumCover()
                .setPath(coverPath);
    }

    private boolean isImage(Path path) {
        try {
            final String contentType = Files.probeContentType(path);
            return contentType != null && contentType.startsWith("image/jpeg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Integer getTrack(final String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        if (value.contains("/")) {
            return Integer.parseInt(value.substring(0, value.indexOf('/')));
        }
        return Integer.parseInt(value);
    }

    @Data
    @Accessors(chain = true)
    public static class AlbumCover {
        private Path path;
    }

    @Data
    @Accessors(fluent = true)
    public static class Config {
        public static Config defaults() {
            return new Config();
        }

        private boolean useFileNameAsTrackNum = true;
        private List<String> tags = Collections.emptyList();
        private String albumName;
    }
}
