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

    public void startListening(final HeosChangeListener listener) {
        if (isListening()) {
            return;
        }

        registerForChanges(listener);
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
            changeListener.stop();
            changeListener = null;
        }
    }
}
