package com.bwee.webit.stats;

import com.bwee.webit.auth.AuthenticationService;
import com.bwee.webit.stats.db.UserTrackStatDailyDbService;
import com.bwee.webit.stats.model.TrackStat;
import com.bwee.webit.stats.model.UserTrackStat;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Slf4j
@Service
public class MusicStatsService {

    @Autowired
    private RMap<Long, String> allTrackPlayTimestamps;

    @Autowired
    private RMap<Long, String> userTrackPlayTimestamps;

    @Autowired
    private RMap<Long, String> allAlbumPlayTimestamps;

    @Autowired
    private RMap<Long, String> userAlbumPlayTimestamps;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserTrackStatDailyDbService userTrackStatDailyDbService;

    @Autowired
    private Clock clock;

    public List<TrackStat> getUserTrackPlayStats(final String userId, final int totalDays) {
        return userTrackStatDailyDbService.findByUserId(userId, totalDays).stream()
                .collect(groupingBy(UserTrackStat::getTrackId, summingInt(UserTrackStat::getCount)))
                .entrySet().stream()
                .map(e -> new TrackStat().setTrackId(e.getKey()).setCount(e.getValue()))
                .sorted(Comparator.comparing(TrackStat::getCount))
                .collect(Collectors.toList());
    }

    public List<String> getAllTrackPlayStats(final int totalDays) {
        return emptyList();
    }

    public void recordTrackPlay(final String trackId) {
        final String userId = authenticationService.getLoginUserId();
        log.info("Track play: {} - {}", userId, trackId);
        final long timestamp = timestamp();
        allTrackPlayTimestamps.put(timestamp, trackId);
        userTrackPlayTimestamps.put(timestamp, userId + ":" + trackId);
    }

    public void recordAlbumPlay(final String albumId) {
        final String userId = authenticationService.getLoginUserId();
        log.info("Album play: {} - {}", userId, albumId);
        final long timestamp = timestamp();
        allAlbumPlayTimestamps.put(timestamp, albumId);
        userAlbumPlayTimestamps.put(timestamp, userId + ":" + albumId);
    }

    private Long timestamp() {
        return Instant.now(clock).toEpochMilli();
    }
}
