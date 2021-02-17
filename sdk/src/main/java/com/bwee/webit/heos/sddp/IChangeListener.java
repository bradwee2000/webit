package com.bwee.webit.heos.sddp;

public interface IChangeListener {
    void playerStateChanged(String pid, String state);
    void playerVolumeChanged(String pid, int level);
    void playerNowPlayingChanged(String pid, String nowPlaying);
    void playerNowPlayingProgress(String pid, int current, int duration);
}
