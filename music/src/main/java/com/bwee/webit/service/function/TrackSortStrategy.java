package com.bwee.webit.service.function;

import com.bwee.webit.model.MusicUser;

import java.util.function.Function;

public interface TrackSortStrategy extends Function<MusicUser, MusicUser> {
}
