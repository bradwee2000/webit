package com.bwee.webit.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.List;

import static java.util.Collections.emptyList;

@Data
@Accessors(chain = true)
public class MusicUser {

    public static MusicUser copyOf(final MusicUser model) {
        final MusicUser copy = new MusicUser();
        BeanUtils.copyProperties(model, copy);
        return copy;
    }

    private String id;
    private String name;
    private List<String> trackIdQueue = emptyList();
    private int currentTrackIndex = 0;
    private boolean isShuffle = Boolean.FALSE;
    private boolean isLoop = Boolean.FALSE;
    private boolean isPlaying = Boolean.FALSE;
}
