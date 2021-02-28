package com.bwee.webit.heos.connect;

import com.bwee.webit.heos.commands.SystemCommands;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class HeosListener extends HeosClient {
    private HeosChangeReader changeListener;

    public HeosListener(final Supplier<String> deviceIp) {
        super(deviceIp);
    }

    public void startListening() {
        if (isListening()) {
            return;
        }

        registerForChanges(new HeosChangeListener() {
            @Override
            public void playerStateChanged(final String pid, final String state) {
                log.info("Change player state {} {} ", pid, state);
            }

            @Override
            public void playerVolumeChanged(final String pid, final  int level) {
                log.info("Player volume changed {} {}", pid, level);
            }

            @Override
            public void playerNowPlayingChanged(final String pid) {
                log.info("Now playing changed: {}", pid);
            }

            @Override
            public void playerNowPlayingProgress(final String pid, final int current, final int duration) {
                log.info("Play Progress: {} {} {} ", pid, current, duration);
            }
        });
    }

    public void registerForChanges(final HeosChangeListener listener) {
        if (!isConnected()) {
            connect();
        }

        final Response r = execute(SystemCommands.REGISTER_FOR_CHANGE_EVENTS(true));

        if (r.isSuccess()) {
            changeListener = new HeosChangeReader(getIn(), getGson(), listener);
            new Thread(changeListener).start();
        }
    }

    public boolean isListening() {
        return changeListener != null;
    }

    public void stop() {
        if (isListening()) {
            changeListener.setStop(true);
        }
    }
}
