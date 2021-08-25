package com.bwee.webit.stats.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class UserTrackStat {

    private String userId;
    private String trackId;
    private LocalDateTime time;
    private Integer count;
    private StatDuration duration;
}
