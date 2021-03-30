package com.bwee.webit.heos.connect;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static java.lang.Integer.parseInt;

@Slf4j
@ToString
public class HeosChangeReader implements Runnable {
    private final HeosChangeListener listener;
    private final HeosConnection heosConnection;
    private boolean isStop = false;

    public HeosChangeReader(final HeosConnection heosConnection,
                            final HeosChangeListener listener) {
        this.heosConnection = heosConnection;
        this.listener = listener;
    }

    @Override
    public void run() {
        while (!isStop) {
            readNextEvent();
        }
    }

    private void readNextEvent() {
        // wait and read next input
        final Response res = heosConnection.read(Response.class);

        final Map<String, String> map = res.getMessageParams();

        final String pid = map.get("pid");
        log.info("COMMAND: {} - {}", res.getCommand(), res.getMessageParams());

        switch (res.getCommand()) {
            case Events.PLAYER_STATE_CHANGED:
                final String state = map.get("state");
                listener.playerStateChanged(pid, state);
                break;
            case Events.PLAYER_VOLUME_CHANGED:
                int level = parseInt(map.get("level"));
                listener.playerVolumeChanged(pid, level);
                break;
            case Events.PLAYER_NOW_PLAYING_CHANGED:
                log.info("RES: {}", map);
                listener.playerNowPlayingChanged(pid);
                break;
            case Events.PLAYER_NOW_PLAYING_PROGRESS:
                int current = parseInt(map.get("cur_pos"));
                int duration = parseInt(map.get("duration"));
                listener.playerNowPlayingProgress(pid, current, duration);
                break;
            case Events.PLAYER_PLAYBACK_ERROR:
                listener.playerPlaybackError(pid, map.get("error"));
        }
    }

    public void stop() {
        isStop = true;
        heosConnection.close();
    }
}