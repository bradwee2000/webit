package com.bwee.webit.heos;

import com.bwee.webit.heos.sddp.Response;
import com.bwee.webit.heos.sddp.TelnetConnection;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class Player {
    private final String name;
    private final String pid;
    private final String model;
    private final String version;
    private Double gid;

    /**
     * Constructor to be called from the 'getPlayers' response. (See HeosSystem -> getPlayers)
     * @param map
     */
    public Player(Map<String, Object> map){
        this.name = (String) map.get("name");
        // For some reason pid gets cast to a double by default, leaving a dot (.) in it.
        // For operations, it should just be a String
        this.pid = String.valueOf((Double) map.get("pid")).replace(".", "");
        this.model = (String) map.get("model");
        this.version = (String) map.get("version");

        // Gid is not always contained.
        if(map.containsKey("gid")){
            this.gid = (Double) map.get("gid");
        }
    }

    public Player(String name) {
        this.name = name;
        this.pid = "";
        this.model = "";
        this.version = "";
        this.gid = 0.0;
    }

    public String getName(){
        return name;
    }

    public String getPid() {
        return pid;
    }

    public String getModel() {
        return model;
    }

    public String getVersion() {
        return version;
    }

    public Double getGid() {
        return gid;
    }

    /**
     * Get this player's current state.
     * @return a String that is either "start", "stop", or "pause".
     */
    public String getPlayState(){
        Response response = TelnetConnection.write(PlayerCommands.GET_PLAY_STATE(this.pid));

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
    public String setPlayState(PlayState state){
        Response response = TelnetConnection.write(PlayerCommands.SET_PLAY_STATE(this.pid, state));

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
    public int getVolume(){
        Response response = TelnetConnection.write(PlayerCommands.GET_VOLUME(this.pid));

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
    public int setVolume(int newVolume){
        Response response = TelnetConnection.write(PlayerCommands.SET_VOLUME(this.pid, newVolume));

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
    public boolean volumeUp(int step){
        Response response = TelnetConnection.write(PlayerCommands.VOLUME_UP(this.pid, step));
        return response.isSuccess();
    }

    /**
     * Ticks the volume down by a certain step.
     * @param step (between 1-10)
     * @return boolean indicating a successful operation.
     */
    public boolean volumeDown(int step){
        Response response = TelnetConnection.write(PlayerCommands.VOLUME_DOWN(this.pid, step));
        return response.isSuccess();
    }

    /**
     * Gets the now playing media in the format of:
     * 'Song Name' by Song Artist
     * @return Now playing media in a formatted String.
     */
    public String getNowPlayingMedia(){
        Response response = TelnetConnection.write(PlayerCommands.GET_NOW_PLAYING_MEDIA(this.pid));

        if(response.isSuccess()){
            Map<String, Object> map = (Map<String, Object>) response.getPayload();
            return "'" + map.get("song") + "' by "+ map.get("artist");
        }

        return null;
    }

    public List<QueuedSong> getQueue() {
        final Type type = new TypeToken<Response<List<QueuedSong>>>() {}.getType();
        final Response<List<QueuedSong>> response = TelnetConnection.execute(PlayerCommands.GET_QUEUE(this.pid), type);

        if (!response.isSuccess()){
             log.error("Failed to get queue. Player Id={}, message={}", pid, response.getMessage());
             return Collections.emptyList();
        }

        return response.getPayload();
    }

    public boolean playNext() {
        Response response = TelnetConnection.write(PlayerCommands.PLAY_NEXT(this.pid));
        return response.isSuccess();
    }

    public boolean playPrevious() {
        Response response = TelnetConnection.write(PlayerCommands.PLAY_PREVIOUS(this.pid));
        return response.isSuccess();
    }

    public boolean playUrl(final String url) {
        Response response = TelnetConnection.write(BrowseCommands.PLAY_URL(this.pid, url));
        return response.isSuccess();
    }
}
