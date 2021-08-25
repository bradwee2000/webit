package com.bwee.webit.config;

import com.bwee.webit.file.MusicFileService;
import com.bwee.webit.model.Album;
import com.bwee.webit.model.SearchType;
import com.bwee.webit.search.query.*;
import com.bwee.webit.service.*;
import com.bwee.webit.service.strategy.year.MultiYearParser;
import com.bwee.webit.service.strategy.year.StringToDateParser;
import com.bwee.webit.service.strategy.year.StringToIntYearParser;
import com.bwee.webit.service.strategy.year.YearParser;
import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;
import java.time.Clock;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
public class MusicConfiguration {
    @Value("${music.storage.path:~/}")
    private String musicStoragePath;

    @Value("${music.play.code.change.interval.sec:300}")
    private int playCodeChangeIntervalSec;

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
    public Mp3Reader mp3Reader() {
        final List<YearParser> yearParsers = Collections.unmodifiableList(Arrays.asList(
                new StringToIntYearParser(),
                new StringToDateParser()
        ));
        return new Mp3Reader(new MultiYearParser(yearParsers));
    }

    @Bean
    public MusicFileService musicFileService(final FileService fileService) {
        return new MusicFileService(fileService, musicStoragePath);
    }

    @Bean
    public PlayCodeGenerator playCodeGenerator(final MessageDigest md,
                                               final Clock clock) {
        return new PlayCodeGenerator(md, clock, playCodeChangeIntervalSec);
    }

    @Bean
    @ConditionalOnMissingBean(Clock.class)
    public Clock clock() {
        return Clock.system(ZoneOffset.UTC);
    }

    @Bean
    @ConditionalOnMissingBean(FileService.class)
    public FileService fileService() {
        return new FileService();
    }

    @Bean("albumQueryStrategies")
    public Map<SearchType, QueryStrategy> albumQueryStrategies(
            final AlbumQueryStrategy albumQueryStrategy,
            final ArtistQueryStrategy artistQueryStrategy
    ) {
        return ImmutableMap.<SearchType, QueryStrategy>builder()
                .put(SearchType.allFields, albumQueryStrategy)
                .put(SearchType.artist, artistQueryStrategy)
                .build();
    }

    @Bean("trackQueryStrategies")
    public Map<SearchType, QueryStrategy> trackQueryStrategies(
            final TrackQueryStrategy trackQueryStrategy,
            final ArtistQueryStrategy artistQueryStrategy
    ) {
        return ImmutableMap.<SearchType, QueryStrategy>builder()
                .put(SearchType.allFields, trackQueryStrategy)
                .put(SearchType.artist, artistQueryStrategy)
                .build();
    }

    @Bean
    public AlbumQueryStrategy albumQueryStrategy() {
        return new AlbumQueryStrategy();
    }

    @Bean
    public ArtistQueryStrategy artistQueryStrategy() {
        return new ArtistQueryStrategy();
    }

    @Bean
    public TrackQueryStrategy trackQueryStrategy() {
        return new TrackQueryStrategy();
    }
}
