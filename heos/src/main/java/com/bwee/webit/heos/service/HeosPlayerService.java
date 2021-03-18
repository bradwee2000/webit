package com.bwee.webit.heos.service;

import com.bwee.webit.heos.commands.BrowseCommands;
import com.bwee.webit.heos.commands.PlayerCommands;
import com.bwee.webit.heos.connect.Response;
import com.bwee.webit.heos.model.AddCriteria;
import com.bwee.webit.heos.model.PlayState;
import com.bwee.webit.heos.model.QueuedSong;
import com.bwee.webit.heos.connect.HeosClient;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HeosPlayerService {

    @Autowired
    private HeosClient heosClient;

    @Autowired
    private HeosSystemService systemService;

    public PlayState getPlayState(String playerId){
        final Response response = heosClient.execute(PlayerCommands.GET_PLAY_STATE(playerId));
        if(response.isSuccess()) {
            return PlayState.valueOf(response.getMessageParam("state"));
        }
        return null;
    }

    /**
     * Will set this player's state to the parameter.
     * @param state - a String that is either "start", "stop", or "pause".
     * @return the newly set state, a String that is either "start", "stop", or "pause".
     */
    public boolean setPlayState(String playerId, PlayState state){
        final Response response = heosClient.execute(PlayerCommands.SET_PLAY_STATE(playerId, state));

        if(!response.isSuccess()){
            log.error("Failed to change play state: {}", response);
        }

        return response.isSuccess();
    }

    /**
     * Gets this player's volume.
     * @return the volume (0-100). -1 if operation failed.
     */
    public int getVolume(final String playerId) {
        final Response response = heosClient.execute(PlayerCommands.GET_VOLUME(playerId));

        if (response.isSuccess()){
            return Integer.parseInt(response.getMessageParam("level"));
        }
        return -1;
    }

    /**
     * Sets this player's volume.
     * @param newVolume (between 0-100).
     * @return The volume after setting it. -1 if operation failed.
     */
    public int setVolume(String playerId, int newVolume){
        final Response response = heosClient.execute(PlayerCommands.SET_VOLUME(playerId, newVolume));

        if(response.isSuccess()){
            return Integer.parseInt(response.getMessageParam("level"));
        }
        return -1;
    }

    /**
     * Ticks the volume up by a certain step.
     * @param step (between 1-10)
     * @return boolean indicating a successful operation.
     */
    public boolean volumeUp(String playerId, int step){
        Response response = heosClient.execute(PlayerCommands.VOLUME_UP(playerId, step));
        return response.isSuccess();
    }

    /**
     * Ticks the volume down by a certain step.
     * @param step (between 1-10)
     * @return boolean indicating a successful operation.
     */
    public boolean volumeDown(String playerId, int step){
        Response response = heosClient.execute(PlayerCommands.VOLUME_DOWN(playerId, step));
        return response.isSuccess();
    }

    /**
     * Gets the now playing media in the format of:
     * 'Song Name' by Song Artist
     * @return Now playing media in a formatted String.
     */
    public String getNowPlayingMedia(String playerId){
        Response response = heosClient.execute(PlayerCommands.GET_NOW_PLAYING_MEDIA(playerId));

        if(response.isSuccess()){
            Map<String, Object> map = (Map<String, Object>) response.getPayload();
            return "'" + map.get("song") + "' by "+ map.get("artist");
        }

        return null;
    }

    public List<QueuedSong> getQueue(String playerId) {
        final Type type = new TypeToken<Response<List<QueuedSong>>>() {}.getType();
        final Response<List<QueuedSong>> response = heosClient.execute(PlayerCommands.GET_QUEUE(playerId), type);

        if (!response.isSuccess()){
            log.error("Failed to get queue. Player Id={}, message={}", playerId, response.getMessage());
            return Collections.emptyList();
        }

        return response.getPayload();
    }

    public boolean playNext(String playerId) {
        final Response response = heosClient.execute(PlayerCommands.PLAY_NEXT(playerId));
        return response.isSuccess();
    }

    public boolean playPrevious(String playerId) {
        final Response response = heosClient.execute(PlayerCommands.PLAY_PREVIOUS(playerId));
        return response.isSuccess();
    }

    public boolean playUrl(String playerId, final String url) {
        log.info("Player {} playing URL {}", playerId, url);
        final Response response = heosClient.execute(BrowseCommands.PLAY_URL(playerId, url));
        return response.isSuccess();
    }

    public boolean addContainerToQueue(String playerId, String sourceId, String containerId, AddCriteria addCriteria) {
        final Response response = heosClient.execute(BrowseCommands.ADD_CONTAINER_TO_QUEUE(
                playerId, sourceId, containerId, addCriteria));
        return response.isSuccess();
    }

    public boolean playUrl(final String playerId, final URL url) {
        final Response response = heosClient.execute(BrowseCommands.PLAY_URL(playerId, url.toString()));
        return response.isSuccess();
    }
}
