package com.bwee.webit.service.strategy.sort;

import com.bwee.webit.model.MusicUser;

import java.util.function.Function;

public interface TrackSortStrategy extends Function<MusicUser, MusicUser> {
}
