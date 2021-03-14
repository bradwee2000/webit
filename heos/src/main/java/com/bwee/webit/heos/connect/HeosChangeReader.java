package com.bwee.webit.heos.connect;

import com.google.gson.Gson;

import java.util.Map;
import java.util.Scanner;

public class HeosChangeReader implements Runnable {

    private final Scanner in;
    private final Gson gson;
    private final HeosChangeListener listener;

    private boolean isStop = false;

    public HeosChangeReader(final Scanner in, final Gson gson, final HeosChangeListener listener) {
        this.in = in;
        this.gson = gson;
        this.listener = listener;
    }

    @Override
    public void run() {
        while (!isStop) {
            // wait and read next input
            final Response res = gson.fromJson(in.next(), Response.class);

            final Map<String, String> map = res.getMessageParams();

            final String pid = map.get("pid");

            switch (res.getCommand()) {
                case Events.PLAYER_STATE_CHANGED:
                    final String state = map.get("state");
                    listener.playerStateChanged(pid, state);
                    break;
                case Events.PLAYER_VOLUME_CHANGED:
                    int level = Integer.parseInt(map.get("level"));
                    listener.playerVolumeChanged(pid, level);
                    break;
                case Events.PLAYER_NOW_PLAYING_CHANGED:
                    listener.playerNowPlayingChanged(pid);
                    break;
                case Events.PLAYER_NOW_PLAYING_PROGRESS:
                    int current = Integer.parseInt(map.get("cur_pos"));
                    int duration = Integer.parseInt(map.get("duration"));
                    listener.playerNowPlayingProgress(pid, current, duration);
                    break;
            }
        }
    }

    public void stop() {
        isStop = true;
    }
}