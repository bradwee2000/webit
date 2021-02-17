package com.bwee.webit.heos;

import com.bwee.webit.heos.sddp.HeosClient;
import com.bwee.webit.heos.sddp.Response;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HeosPlayerService {

    @Autowired
    private HeosClient heosClient;

    public String getPlayState(String playerId){
        Response response = heosClient.write(PlayerCommands.GET_PLAY_STATE(playerId));
        if(response.isSuccess()){
            if(response.getMessage().contains("state=")){
                String state = response.getMessage().substring(response.getMessage().indexOf("state=") + 6);
                return state;
            }
        }
        return null;
    }

    /**
     * Will set this player's state to the parameter.
     * @param state - a String that is either "start", "stop", or "pause".
     * @return the newly set state, a String that is either "start", "stop", or "pause".
     */
    public String setPlayState(String playerId, PlayState state){
        Response response = heosClient.write(PlayerCommands.SET_PLAY_STATE(playerId, state));

        if(response.isSuccess()){
            if(response.getMessage().contains("state=")){
                String currentState = response.getMessage().substring(response.getMessage().indexOf("state=") + 6);
                return currentState;
            }
        }
        return null;
    }

    /**
     * Gets this player's volume.
     * @return the volume (0-100). -1 if operation failed.
     */
    public int getVolume(String playerId) {
        Response response = heosClient.write(PlayerCommands.GET_VOLUME(playerId));

        if(response.isSuccess()){
            if(response.getMessage().contains("level=")){
                String currentVolumeString = response.getMessage().substring(response.getMessage().indexOf("level=") + 6);
                int currentVolume = Integer.parseInt(currentVolumeString);
                return currentVolume;
            }
        }
        return -1;
    }

    /**
     * Sets this player's volume.
     * @param newVolume (between 0-100).
     * @return The volume after setting it. -1 if operation failed.
     */
    public int setVolume(String playerId, int newVolume){
        Response response = heosClient.write(PlayerCommands.SET_VOLUME(playerId, newVolume));

        if(response.isSuccess()){
            if(response.getMessage().contains("level=")){
                String currentVolumeString = response.getMessage().substring(response.getMessage().indexOf("level=") + 6);
                int currentVolume = Integer.parseInt(currentVolumeString);
                return currentVolume;
            }
        }
        return -1;
    }

    /**
     * Ticks the volume up by a certain step.
     * @param step (between 1-10)
     * @return boolean indicating a successful operation.
     */
    public boolean volumeUp(String playerId, int step){
        Response response = heosClient.write(PlayerCommands.VOLUME_UP(playerId, step));
        return response.isSuccess();
    }

    /**
     * Ticks the volume down by a certain step.
     * @param step (between 1-10)
     * @return boolean indicating a successful operation.
     */
    public boolean volumeDown(String playerId, int step){
        Response response = heosClient.write(PlayerCommands.VOLUME_DOWN(playerId, step));
        return response.isSuccess();
    }

    /**
     * Gets the now playing media in the format of:
     * 'Song Name' by Song Artist
     * @return Now playing media in a formatted String.
     */
    public String getNowPlayingMedia(String playerId){
        Response response = heosClient.write(PlayerCommands.GET_NOW_PLAYING_MEDIA(playerId));

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
        Response response = heosClient.write(PlayerCommands.PLAY_NEXT(playerId));
        return response.isSuccess();
    }

    public boolean playPrevious(String playerId) {
        Response response = heosClient.write(PlayerCommands.PLAY_PREVIOUS(playerId));
        return response.isSuccess();
    }

    public boolean playUrl(String playerId, final String url) {
        Response response = heosClient.write(BrowseCommands.PLAY_URL(playerId, url));
        return response.isSuccess();
    }
}
