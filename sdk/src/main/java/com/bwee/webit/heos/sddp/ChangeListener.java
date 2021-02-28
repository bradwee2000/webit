package com.bwee.webit.heos.sddp;

public interface ChangeListener {
    void playerStateChanged(String pid, String state);
    void playerVolumeChanged(String pid, int level);
    void playerNowPlayingChanged(String pid);
    void playerNowPlayingProgress(String pid, int current, int duration);
}
