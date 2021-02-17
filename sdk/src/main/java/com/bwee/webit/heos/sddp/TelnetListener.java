package com.bwee.webit.heos.sddp;

import com.bwee.webit.heos.SystemCommands;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

@Slf4j
public class TelnetListener {

    /* Networking */
    private static Socket socket;
    private static PrintWriter out;
    private static Scanner in;

    private static Gson gson;

    /**
     * Connect to a Heos System that is located at a certain ip.
     * @param ipAddress a valid IP address.
     */
    public static void connect(final String ipAddress) {
        try {
            socket = new Socket(ipAddress, 1255);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());
            in.useDelimiter("\r\n");
            gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * Write a certain command to the Heos System.
     * @param command a String describing the command (see Constants package)
     * @return a Response
     */
    public static Response write(final String command) {
        if (socket == null || out == null || in == null || gson == null) {
            return null;
        }

        new Thread(() -> {
            out.println(command);
            out.flush();
        }).start();

        return gson.fromJson(in.next(), Response.class);
    }
    /**
     * Register a certain listener for change events. For now, the runnable is implemented inside this code block.
     * @param listener any class that implements IChangeListener
     */
    public static void registerForChanges(final IChangeListener listener) {
        if (socket == null || in == null || listener == null) {
            return;
        }

        Response r = TelnetListener.write(SystemCommands.REGISTER_FOR_CHANGE_EVENTS(true));

        if (r.isSuccess()) {
            ChangeListenerRunnable run = new ChangeListenerRunnable();
            run.gson = gson;
            run.in = in;
            run.listener = listener;

            new Thread(run).start();
        }
    }
}
