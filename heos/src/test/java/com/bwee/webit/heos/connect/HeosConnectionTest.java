package com.bwee.webit.heos.connect;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Slf4j
class HeosConnectionTest {

    private BlockingQueue<HeosConnection> connectionPool;
    private ExecutorService executorService;
    private Socket socket;
    private AtomicInteger connectCount;
    private Supplier<Socket> socketSupplier;

    @SneakyThrows
    @BeforeEach
    public void before() {
        socket = mock(Socket.class, RETURNS_MOCKS);
        when(socket.isConnected()).thenReturn(Boolean.TRUE);
        when(socket.getInputStream()).thenReturn(System.in);
        when(socket.getOutputStream()).thenReturn(System.out);

        connectCount = new AtomicInteger(0);
        socketSupplier = () -> {
            connectCount.incrementAndGet();
            return socket;
        };

        connectionPool = new ArrayBlockingQueue<>(2);
        executorService = Executors.newFixedThreadPool(1);
    }

    @Test
    public void testCreate_shouldNotConnect() {
        final HeosConnection connection = new HeosConnection(socketSupplier, connectionPool, () -> executorService);
        assertThat(connection.isConnected()).isFalse();
    }

    @Test
    public void testConnect_shouldBeConnected() {

        final HeosConnection connection = new HeosConnection(socketSupplier, connectionPool, () -> executorService);
        assertThat(connection.connect().isConnected()).isTrue();
    }

    @Test
    public void testConnectMultipleTimes_shouldConnectOnlyOnce() {
        new HeosConnection(socketSupplier, connectionPool, () -> executorService).connect().connect().connect();
        assertThat(connectCount.get()).isEqualTo(1);
    }

    @Test
    public void testFree_shouldReturnConnectionToPool() {
        final HeosConnection connection = new HeosConnection(socketSupplier, connectionPool, () -> executorService);
        connection.free();
        assertThat(connectionPool).hasSize(1);
    }

    @Test
    public void testCloseWithoutConnecting_shouldNotThrowException() {
        new HeosConnection(socketSupplier, connectionPool, () -> executorService).close();
    }

    @Test
    public void testConnectAndCloseMultipleTimes_shouldConnectAndCloseAndNotThrowException() {
        new HeosConnection(socketSupplier, connectionPool, () -> executorService).connect().close().connect().close();
        assertThat(connectCount.get()).isEqualTo(2);
    }

    @Test
    @SneakyThrows
    public void testRead_shouldRead() {
        final ByteArrayInputStream is = new ByteArrayInputStream("{ \"payload\":\"Hello!\"}".getBytes());
        when(socket.getInputStream()).thenReturn(is);

        final HeosConnection connection = new HeosConnection(socketSupplier, connectionPool, () -> executorService).connect();
        final Response res = connection.read(Response.class);
        assertThat(res).isNotNull().extracting(r -> r.getPayload()).isEqualTo("Hello!");
    }

    @Test
    @SneakyThrows
    public void testCancelRead_shouldUnblockAndReturnNull() {
        final HeosConnection connection = new HeosConnection(socketSupplier, connectionPool, () -> executorService).connect();
        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> connection.close());
        final Response res = connection.read(Response.class);
        assertThat(res).isNotNull();
    }
}