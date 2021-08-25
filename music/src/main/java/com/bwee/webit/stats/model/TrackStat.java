package com.bwee.webit.stats.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TrackStat {

    private String trackId;
    private int count;
}
