package com.bwee.webit.heos.service;

import com.bwee.webit.heos.client.MusicServiceClient;
import com.bwee.webit.heos.client.model.MusicUserRes;
import com.bwee.webit.heos.connect.HeosChangeListener;
import com.bwee.webit.heos.connect.HeosClient;
import com.bwee.webit.heos.connect.HeosListener;
import com.bwee.webit.heos.model.PlayState;
import com.bwee.webit.heos.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HeosMusicService implements HeosChangeListener {

    @Autowired
    private HeosListener heosListener;

    @Autowired
    private HeosClient heosClient;

    @Autowired
    private HeosPlayerService playerService;

    @Autowired
    private HeosSystemService systemService;

    @Autowired
    private MusicServiceClient musicServiceClient;

    public String lastTokenUsed;

    public void connect() {
        heosClient.connect();
        heosListener.startListening(this);
    }

    public void close() {
        heosClient.close();
        heosListener.stop();
    }

    public boolean play(final String pid, final String token) {
        lastTokenUsed = token;
        final MusicUserRes user = musicServiceClient.get(token);
        final String playCode = musicServiceClient.getPlayCode(token).getPlayCode();
        final String trackId = user.getSelectedTrack().getId();
        final String playToken = toPlayToken(playCode, trackId);
        final String url = "http://192.168.1.11:8080/music/tracks/" + trackId + "/stream?token=" + playToken;
        return playerService.playUrl(pid, url);
    }

    public List<Player> getPlayers() {
        return systemService.getPlayers();
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

    @Override
    public void playerStateChanged(String pid, String state) {
        log.info("Change player state {} {} ", pid, state);
    }

    @Override
    public void playerVolumeChanged(String pid, int level) {
        log.info("Player volume changed {} {}", pid, level);
    }

    @Override
    public void playerNowPlayingChanged(String pid) {
        log.info("Now playing changed: {}", pid);
    }

    @Override
    public void playerNowPlayingProgress(String pid, int current, int duration) {
        log.info("Play Progress: {} {} {} ", pid, current, duration);
        if (current == duration) {
            log.info("Play next song");
            play(pid, lastTokenUsed);
        }
    }
}
