package com.bwee.webit.heos;

import com.bwee.webit.heos.sddp.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class HeosSystem {

    private HeosClient heosClient;

    public HeosSystem(HeosClient client) {
        this.heosClient = client;
    }

    public HeosSystem(String ipAddress){
        TelnetConnection.connect(ipAddress);
        TelnetListener.connect(ipAddress);
        TelnetListener.registerForChanges(new IChangeListener() {
            @Override
            public void playerStateChanged(String pid, String state) {
                log.info("Change player state {} {} ", pid, state);
            }

            @Override
            public void playerVolumeChanged(String pid, int level) {
                log.info("Player volume changed {} {}", pid, level);
            }

            @Override
            public void playerNowPlayingChanged(String pid, String nowPlaying) {
                log.info("Now playing: {} {} ", pid, nowPlaying);
            }

            @Override
            public void playerNowPlayingProgress(String pid, int current, int duration) {
                log.info("Play Progress: {} {} {} ", pid, current, duration);
            }
        });
    }

    /**
     * Checks whether or not the user is signed in to its HEOS system.
     * @return The signed in account; if not signed in, null.
     */
    public Account accountCheck(){
        Response response = heosClient.write(SystemCommands.ACCOUNT_CHECK);

        if(response.getResult().equals(Results.SUCCESS)){
            if(response.getMessage().contains("signed_in")){
                String username = response.getMessage().substring(response.getMessage().indexOf("un=") + 3);
                return new Account(username);
            }
        }
        return null;
    }

    /**
     * Sign in to the HEOS system with specified HEOS account.
     * @param username
     * @param password
     * @return The signed in account; if not signed in, null.
     */
    public boolean accountSignIn(String username, String password){
        Response response = heosClient.write(SystemCommands.ACCOUNT_SIGN_IN(username, password));
        log.info("Sign In Response: {}", response.getMessage());
        return response.isSuccess() && !response.getMessage().contains("command under process");
    }

    /**
     * Sign out of the HEOS system.
     * @return boolean indicating a successful operation.
     */
    public boolean accountSignOut(){
        Response response = heosClient.write(SystemCommands.ACCOUNT_SIGN_OUT);
        return response.isSuccess() && response.getMessage().equals("signed_out");
    }

    /**
     * Gets the HEOS System state
     * @return boolean indicating if the system is alive.
     */
    public boolean systemHeartBeat(){
        Response response = heosClient.write(SystemCommands.HEARTBEAT);
        return response.isSuccess();
    }

    /**
     * Gets the HEOS players that are connected to this system.
     * @return Players. If none, null.
     */
    public List<Player> getPlayers(){
        Response response = heosClient.write(SystemCommands.GET_PLAYERS);

        if(response.isSuccess()){
            List<Player> players = new ArrayList<>();
            for (Map<String, Object> map : (List<Map<String, Object>>) response.getPayload() ) {
                players.add(new Player(map));
            }
            return players;
        }
        return Collections.emptyList();
    }

    public List<MusicSource> getMusicSources() {
        Type type = new TypeToken<Response<List<MusicSource>>>() {}.getType();
        Response<List<MusicSource>> response = heosClient.execute(BrowseCommands.GET_MUSIC_SOURCES(), type);
        return response.isSuccess() ? response.getPayload() : Collections.emptyList();
    }
}
