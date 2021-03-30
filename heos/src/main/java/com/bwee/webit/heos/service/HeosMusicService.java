package com.bwee.webit.heos.service;

import com.bwee.webit.heos.client.MusicServiceClient;
import com.bwee.webit.heos.client.model.MusicUserRes;
import com.bwee.webit.heos.connect.HeosChangeListener;
import com.bwee.webit.heos.connect.HeosClient;
import com.bwee.webit.heos.model.PlayState;
import com.bwee.webit.heos.model.Player;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HeosMusicService {

    @Autowired
    private HeosClient heosClient;

    @Autowired
    private HeosPlayerService playerService;

    @Autowired
    private HeosSystemService systemService;

    @Autowired
    private MusicServiceClient musicServiceClient;

    private String lastTokenUsed;
    private PlayerState playerState = new PlayerState();

    public boolean play(final String pid, final String token) {
        playerState.setPlayerId(pid);
        playerService.clearQueue(pid);
        lastTokenUsed = token;
        final MusicUserRes user = musicServiceClient.get(token);
        final String playCode = musicServiceClient.getPlayCode(token).getPlayCode();
        final String trackId = user.getSelectedTrack().getId();
        final String playToken = toPlayToken(playCode, trackId);
        final String url = "http://192.168.1.2:8080/music/tracks/" + trackId + "/stream?token=" + playToken;
        return playerService.playUrl(pid, url);
    }

    public List<Player> getPlayers() {
        return systemService.getPlayers();
    }

    public PlayerState getPlayerState(final String pid) {
        return playerState;
    }

    public boolean pause(final String pid) {
        return playerService.setPlayState(pid, PlayState.STOP);
    }

    public int getVolume(final String pid) {
        return playerService.getVolume(pid);
    }

    public int setVolume(final String pid, final int volume) {
        return playerService.setVolume(pid, volume);
    }

    public String toPlayToken(final String playCode, final String trackId) {
        final String hashKey = playCode + ":" + trackId;
        final String playToken = hashKey.hashCode() + "";
        return playToken;
    }

    public boolean startListening() {
        heosClient.listen(new Listener(playerState));
        return true;
    }

    public boolean stopListening() {
        heosClient.stopListening();
        return true;
    }

    @Data
    @Accessors(chain = true)
    public static class Listener implements HeosChangeListener {
        private final PlayerState playerState;

        public Listener(PlayerState playerState) {
            this.playerState = playerState;
        }

        @Override
        public void playerStateChanged(String pid, String state) {
            log.info("Change player state {} {} ", pid, state);
        }

        @Override
        public void playerVolumeChanged(final String pid, final int volume) {
            playerState.setVolume(volume);
        }

        @Override
        public void playerNowPlayingChanged(final String pid) {
            playerState.setCurrentProgress(0).setDuration(0);
        }

        @Override
        public void playerNowPlayingProgress(final String pid, final int current, final int duration) {
            playerState.setCurrentProgress(current).setDuration(duration);
        }

        @Override
        public void playerPlaybackError(final String pid, final String error) {
            log.error("HEOS playback error on pid={}: {}", pid, error);
        }
    }

    @Data
    @Accessors(chain = true)
    public static class PlayerState {
        private String playerId;
        private int volume;
        private int currentProgress;
        private int duration;
    }
}
