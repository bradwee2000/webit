package com.bwee.webit.heos.connect;

import com.bwee.webit.heos.commands.SystemCommands;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Slf4j
public class HeosConnection {
    private final Supplier<Socket> socketSupplier;
    private final BlockingQueue<HeosConnection> connectionPool;
    private final Supplier<ExecutorService> executorServiceSupplier;
    private final Gson gson;

    private ExecutorService executorService;
    private Socket socket;
    private PrintWriter out;
    private Scanner in;
    private CompletableFuture<String> future;

    public HeosConnection(final Supplier<Socket> socketSupplier,
                          final BlockingQueue<HeosConnection> connectionPool,
                          final Supplier<ExecutorService> executorServiceSupplier) {
        this.socketSupplier = socketSupplier;
        this.connectionPool = connectionPool;
        this.executorServiceSupplier = executorServiceSupplier;
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    @SneakyThrows
    public HeosConnection connect() {
        if (isConnected()) {
            return this;
        }
        socket = socketSupplier.get();
        log.info("Connecting to {}:{}", socket.getInetAddress(), socket.getPort());
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new Scanner(socket.getInputStream());
        in.useDelimiter("\r\n");
        executorService = executorServiceSupplier.get();
        return this;
    }

    @SneakyThrows
    public HeosConnection close() {
        if (future != null && !future.isDone() && !future.isCancelled()) {
            try {
                future.cancel(true);
                future.join();
            } catch (final CancellationException e) {
                log.info("Scanner read is cancelled");
            }
        }

        if (isConnected()) {
            log.info("Closing Connection");
            executorService.shutdownNow();
            socket.close();
            in.close();
            out.close();
            in = null;
            out = null;
            socket = null;
        }
        log.info("Finished Closing Connection");
        return this;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public <T> Response<T> read(final Type type) {
        if (!isConnected()) {
            connect();
        }

        try {
            future = CompletableFuture.supplyAsync(() -> in.next(), executorService);
            final String json = future.get();
            log.info("Response: {}", json);
            return gson.fromJson(json, type);
        } catch (final CancellationException e) {
            return new Response<>();
        } catch (final Exception e) {
            log.error("HEOS Error", e);
            return new Response<>();
        }
    }

    public Optional<HeosChangeReader> listen(final HeosChangeListener heosChangeListener) {
        final Response res = executeAndLeaveOpen(SystemCommands.REGISTER_FOR_CHANGE_EVENTS(true), Response.class);
        if (res.isSuccess()) {
            final HeosChangeReader reader = new HeosChangeReader(this, heosChangeListener);

            CompletableFuture.runAsync(reader, executorService).whenComplete((v, e) -> close());

            return Optional.of(reader);
        } else {
            close();
        }
        return Optional.empty();
    }

    private <T> Response<T> executeAndLeaveOpen(final String command, final Type type) {
        if (!isConnected()) {
            connect();
        }

        executorService.submit(() -> {
            out.println(command);
            out.flush();
        });

        return read(type);
    }

    public <T> Response<T> execute(final String command, final Type type) {
        try {
            return executeAndLeaveOpen(command, type);
        } finally {
            free();
        }
    }

    public void free() {
        final boolean isReturnedToPool = connectionPool.offer(this);
        if (!isReturnedToPool) {
            close();
        }
    }
}
