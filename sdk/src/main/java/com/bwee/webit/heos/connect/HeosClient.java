package com.bwee.webit.heos.connect;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Supplier;

@Slf4j
public class HeosClient {
    /* Networking */
    private Socket socket;
    private PrintWriter out;
    private Scanner in;
    private Gson gson;
    private Supplier<String> deviceIp;

    public HeosClient(final Supplier<String> deviceIp) {
        this.deviceIp = deviceIp;
    }

    /**
     * Connect to a Heos System that is located at a certain ip.
     */
    public void connect() {
        try {
            socket = new Socket(deviceIp.get(), 1255);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());
            in.useDelimiter("\r\n");
            gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
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
        if (!isConnected()) {
            connect();
        }

        new Thread(() -> {
            out.println(command);
            out.flush();
        }).start();

        final String json = in.next();
        log.info("Response: {}", json);

        return gson.fromJson(json, type);
    }

    public boolean isConnected() {
        return socket != null && out != null && in != null && gson != null;
    }

    protected Scanner getIn() {
        return in;
    }

    protected Gson getGson() {
        return gson;
    }
}
