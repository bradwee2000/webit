package com.bwee.webit.heos.connect;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Slf4j
public class HeosClient {
    private final Supplier<String> deviceIp;
    private final ExecutorService executorService;
    private Optional<HeosChangeReader> heosChangeReader = Optional.empty();

    private AtomicBoolean isListening = new AtomicBoolean(false);

    private final BlockingQueue<HeosConnection> connectionPool = new ArrayBlockingQueue(2);

    public HeosClient(final Supplier<String> deviceIp,
                      final ExecutorService commandExecutorService) {
        this.deviceIp = deviceIp;
        this.executorService = commandExecutorService;
    }

    /**
     * Write a certain command to the Heos System.
     * @param command a String describing the command (see Constants package)
     * @return a Response
     */
    public Response execute(final String command) {
        return execute(command, Response.class);
    }

    public <T> Response<T> execute(final String command, Type type) {
        return getConnection().execute(command, type);
    }

    public HeosClient listen(final HeosChangeListener listener) {
        if (heosChangeReader.isPresent()) {
            log.warn("Already listening.");
            return this;
        }

        heosChangeReader = getConnection().listen(listener);
        return this;
    }

    public HeosClient stopListening() {
        log.info("Stop Listening Called");
        heosChangeReader.ifPresent(h -> h.stop());
        heosChangeReader = Optional.empty();
        log.info("Stop Listening Finished");
        return this;
    }

    private HeosConnection getConnection() {
        log.info("POOL size={}", connectionPool.size());
        return Optional.ofNullable(connectionPool.poll()).orElseGet(() -> createNewConnection());
    }

    private HeosConnection createNewConnection() {
        final Supplier<Socket> socketSupplier = () -> {
            try {
                return new Socket(deviceIp.get(), 1255);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        final Supplier<ExecutorService> executorServiceSupplier = () -> Executors.newFixedThreadPool(2);
        return new HeosConnection(socketSupplier, connectionPool, executorServiceSupplier);
    }

}
