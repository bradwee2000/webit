package com.bwee.webit.heos.commands;

import com.bwee.webit.heos.model.PlayState;

public class PlayerCommands {
    public static final String GET_PLAY_STATE(String pid){
        return "heos://player/get_play_state?pid=" + pid;
    }

    public static final String SET_PLAY_STATE(String pid, PlayState state){
        return "heos://player/set_play_state?pid=" + pid + "&state=" + state.getCode();
    }

    public static final String GET_VOLUME(String pid){
        return "heos://player/get_volume?pid=" + pid;
    }

    public static final String SET_VOLUME(String pid, int level){
        return "heos://player/set_volume?pid=" + pid + "&level=" + String.valueOf(level);
    }

    public static final String VOLUME_UP(String pid, int step){
        return "heos://player/volume_up?pid=" + pid + "&step=" + String.valueOf(step);
    }

    public static final String VOLUME_DOWN(String pid, int step){
        return "heos://player/volume_down?pid=" + pid + "&step=" + String.valueOf(step);
    }

    public static final String GET_NOW_PLAYING_MEDIA(String pid){
        return "heos://player/get_now_playing_media?pid=" + pid;
    }

    public static final String GET_QUEUE(String pid){
        return "heos://player/get_queue?pid=" + pid;
    }

    public static final String PLAY_NEXT(String pid){
        return "heos://player/play_next?pid=" + pid;
    }

    public static final String PLAY_PREVIOUS(String pid){
        return "heos://player/play_previous?pid=" + pid;
    }
}
