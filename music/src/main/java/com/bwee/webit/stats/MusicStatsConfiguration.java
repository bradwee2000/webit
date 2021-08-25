package com.bwee.webit.stats;

import com.bwee.webit.stats.writer.AllAlbumPlayStatsWriter;
import com.bwee.webit.stats.writer.AllTrackPlayStatsWriter;
import com.bwee.webit.stats.writer.UserAlbumPlayStatsWriter;
import com.bwee.webit.stats.writer.UserTrackPlayStatsWriter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.MapOptions;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import static org.redisson.api.MapOptions.WriteMode.WRITE_BEHIND;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
public class MusicStatsConfiguration {

    @Autowired
    private RedissonClient redissonClient;

    @Bean
    public RMap<Long, String> allTrackPlayTimestamps(final AllTrackPlayStatsWriter writer) {
        final MapOptions<Long, String> options = MapOptions.<Long, String>defaults()
                .writeMode(WRITE_BEHIND)
                .writeBehindBatchSize(100)
                .writeBehindDelay(5000)
                .writer(writer);

        return redissonClient.getMap("music.all.track.play.timestamps", options);
    }

    @Bean
    public RMap<Long, String> userTrackPlayTimestamps(final UserTrackPlayStatsWriter writer) {
        final MapOptions<Long, String> options = MapOptions.<Long, String>defaults()
                .writeMode(WRITE_BEHIND)
                .writeBehindBatchSize(100)
                .writeBehindDelay(5000)
                .writer(writer);

        return redissonClient.getMap("music.user.track.play.timestamps", options);
    }

    @Bean
    public RMap<Long, String> allAlbumPlayTimestamps(final AllAlbumPlayStatsWriter writer) {
        final MapOptions<Long, String> options = MapOptions.<Long, String>defaults()
                .writeMode(WRITE_BEHIND)
                .writeBehindBatchSize(100)
                .writeBehindDelay(5000)
                .writer(writer);

        return redissonClient.getMap("music.all.album.play.timestamps", options);
    }

    @Bean
    public RMap<Long, String> userAlbumPlayTimestamps(final UserAlbumPlayStatsWriter writer) {
        final MapOptions<Long, String> options = MapOptions.<Long, String>defaults()
                .writeMode(WRITE_BEHIND)
                .writeBehindBatchSize(100)
                .writeBehindDelay(5000)
                .writer(writer);

        return redissonClient.getMap("music.user.album.play.timestamps", options);
    }
}
