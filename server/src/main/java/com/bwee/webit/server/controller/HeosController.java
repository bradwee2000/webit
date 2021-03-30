package com.bwee.webit.server.controller;

import com.bwee.webit.heos.connect.HeosClient;
import com.bwee.webit.heos.model.Player;
import com.bwee.webit.heos.service.HeosBrowseService;
import com.bwee.webit.heos.service.HeosMusicService;
import com.bwee.webit.heos.service.HeosPlayerService;
import com.bwee.webit.heos.service.HeosSystemService;
import com.bwee.webit.service.MusicUserService;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/heos")
public class HeosController {

    @Autowired
    private HeosBrowseService browseService;

    @Autowired
    private HeosPlayerService playerService;

    @Autowired
    private HeosSystemService systemService;

    @Autowired
    private HeosMusicService heosMusicService;

    @Autowired
    private HeosClient heosClient;

    @Autowired
    private MusicUserService musicUserService;

    @PostMapping("/connect")
    public ResponseEntity connect() {
        final boolean isSuccess = heosMusicService.startListening();
        return ResponseEntity.ok(new SuccessRes().setSuccess(isSuccess));
    }

    @PostMapping("/close")
    public ResponseEntity close() {
        final boolean isSuccess = heosMusicService.stopListening();
        return ResponseEntity.ok(new SuccessRes().setSuccess(isSuccess));
    }

    @PostMapping("/players/{pid}/play")
    public ResponseEntity play(@PathVariable final String pid,
                               @RequestHeader("Authorization") final String token) {
        final boolean isSuccess = heosMusicService.play(pid, token);
        return ResponseEntity.ok(SuccessRes.of(pid).setSuccess(isSuccess));
    }

    @PostMapping("/players/{pid}/pause")
    public ResponseEntity pause(@PathVariable final String pid) {
        final boolean isSuccess = heosMusicService.pause(pid);
        return ResponseEntity.ok(SuccessRes.of(pid).setSuccess(isSuccess));
    }

    @GetMapping("/players/{pid}/queue")
    public ResponseEntity getQueue(@PathVariable final String pid) {
        return ResponseEntity.ok(playerService.getQueue(pid));
    }

    @DeleteMapping("/players/{pid}/queue")
    public ResponseEntity clearQueue(@PathVariable final String pid) {
        final boolean isSuccess = playerService.clearQueue(pid);
        return ResponseEntity.ok(SuccessRes.of(pid).setSuccess(isSuccess));
    }

    @GetMapping("/players")
    public ResponseEntity getPlayers() {
        return ResponseEntity.ok(heosMusicService.getPlayers().stream().map(PlayerResp::new).collect(Collectors.toList()));
    }

    @PostMapping("/players/{pid}/play-url")
    public ResponseEntity playUrl(@PathVariable("pid") final String pid, @RequestParam("url") final String url) {
        final boolean isSuccess = playerService.playUrl(pid, url);
        return ResponseEntity.ok(SuccessRes.of(pid).setSuccess(isSuccess));
    }

    @GetMapping("/players/{pid}/state")
    public ResponseEntity getPlayerState(@PathVariable("pid") final String pid) {
        return ResponseEntity.ok(heosMusicService.getPlayerState(pid));
    }

    @GetMapping("/players/{pid}/volume")
    public ResponseEntity getPlayerVolume(@PathVariable("pid") final String pid) {
        final int volume = heosMusicService.getVolume(pid);
        return ResponseEntity.ok(VolumeRes.of(pid).setVolume(volume));
    }

    @PostMapping("/players/{pid}/volume")
    public ResponseEntity setPlayerVolume(@PathVariable("pid") final String pid,
                                          @RequestParam("volume") final int volume) {
        final int setVolume = heosMusicService.setVolume(pid, volume);
        return ResponseEntity.ok(VolumeRes.of(pid).setVolume(setVolume));
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestHeader final String username,
                                 @RequestHeader final String password) {
        final boolean isSuccess = systemService.accountSignIn(username, password);
        return ResponseEntity.ok(new SuccessRes().setSuccess(isSuccess));
    }

    @GetMapping("/sources")
    public ResponseEntity getSources() {
        return ResponseEntity.ok(systemService.getMusicSources());
    }

    @GetMapping("/sources/{sourceId}")
    public ResponseEntity browse(@PathVariable final String sourceId) {
        return ResponseEntity.ok(browseService.browseSource(sourceId));
    }

    @GetMapping("/sources/{sourceId}/{containerId}")
    public ResponseEntity browse(@PathVariable final String sourceId, @PathVariable final String containerId) {
        return ResponseEntity.ok(browseService.browseContainer(sourceId, containerId));
    }

    @Data
    @Accessors(chain = true)
    public static class PlayerResp {
        private String pid;
        private String model;
        private String name;
        private Double gid;

        public PlayerResp(final Player player) {
            this.pid = player.getPid();
            this.model = player.getModel();
            this.name = player.getName();
            this.gid = player.getGid();
        }
    }

    @Data
    @Accessors(chain = true)
    public static class SuccessRes {
        public static SuccessRes of(final String playerId) {
            return new SuccessRes().setPlayerId(playerId);
        }

        private String playerId;
        private boolean isSuccess = true;
    }

    @Data
    @Accessors(chain = true)
    public static class VolumeRes {
        public static VolumeRes of(final String playerId) {
            return new VolumeRes().setPlayerId(playerId);
        }

        private String playerId;
        private int volume;
    }
}
