package com.bwee.webit.config;

import com.bwee.webit.file.MusicFileService;
import com.bwee.webit.search.query.AlbumQueryStrategy;
import com.bwee.webit.search.query.TrackQueryStrategy;
import com.bwee.webit.search.query.SimpleAlbumQueryStrategy;
import com.bwee.webit.search.query.SimpleTrackQueryStrategy;
import com.bwee.webit.service.AlbumIdGenerator;
import com.bwee.webit.service.FileService;
import com.bwee.webit.service.Mp3Reader;
import com.bwee.webit.service.TrackIdGenerator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;

@Configuration
public class MusicConfiguration {
    @Value("${music.storage.path:~/}")
    private String musicStoragePath;

    @Bean
    @SneakyThrows
    public MessageDigest messageDigest() {
        return MessageDigest.getInstance("SHA-1");
    }

    @Bean
    public TrackIdGenerator musicIdGenerator(final MessageDigest md) {
        return new TrackIdGenerator(md);
    }

    @Bean
    public AlbumIdGenerator albumIdGenerator(final MessageDigest md) {
        return new AlbumIdGenerator(md);
    }

    @Bean
    public Mp3Reader mp3Reader(final TrackIdGenerator trackIdGenerator) {
        return new Mp3Reader(trackIdGenerator);
    }

    @Bean
    public AlbumQueryStrategy searchAlbumQueryStrategy() {
        return new SimpleAlbumQueryStrategy();
    }

    @Bean
    public TrackQueryStrategy searchMusicQueryStrategy() {
        return new SimpleTrackQueryStrategy();
    }

    @Bean
    public MusicFileService musicFileService(final FileService fileService) {
        return new MusicFileService(fileService, musicStoragePath);
    }
}
