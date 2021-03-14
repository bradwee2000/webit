package com.bwee.webit.server.controller;

import com.bwee.webit.heos.model.Player;
import com.bwee.webit.heos.connect.HeosClient;
import com.bwee.webit.heos.connect.HeosListener;
import com.bwee.webit.heos.service.HeosMusicService;
import com.bwee.webit.heos.service.HeosBrowseService;
import com.bwee.webit.heos.service.HeosPlayerService;
import com.bwee.webit.heos.service.HeosSystemService;
import com.bwee.webit.service.MusicUserService;
import lombok.Data;
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
    private HeosListener heosListener;

    @Autowired
    private MusicUserService musicUserService;

    @PostMapping("/players/{pid}/connect")
    public ResponseEntity connect(@PathVariable final String pid) {
        heosMusicService.connect(pid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/players/{pid}/play")
    public ResponseEntity play(@PathVariable final String pid,
                               @RequestHeader("Authorization") final String token) {
        final boolean isSuccess = heosMusicService.play(pid, token);
        return ResponseEntity.ok(isSuccess);
    }

    @PostMapping("/players/{pid}/pause")
    public ResponseEntity pause(@PathVariable final String pid) {
        final String state = heosMusicService.pause(pid);
        return ResponseEntity.ok(state);
    }

    @GetMapping("/players")
    public ResponseEntity getPlayers() {
        return ResponseEntity.ok(heosMusicService.getPlayers().stream().map(PlayerResp::new).collect(Collectors.toList()));
    }

    @GetMapping("/players/{pid}/now-playing")
    public ResponseEntity getPlayer(@PathVariable("pid") final String pid) {
        return ResponseEntity.ok(playerService.getNowPlayingMedia(pid));
    }

    @PostMapping("/players/{pid}/play-url")
    public ResponseEntity playUrl(@PathVariable("pid") final String pid, @RequestParam("url") final String url) {
        return ResponseEntity.ok(playerService.playUrl(pid, url));
    }

    @GetMapping("/players/{pid}/volume")
    public ResponseEntity getPlayerVolume(@PathVariable("pid") final String pid) {
        return ResponseEntity.ok(heosMusicService.getVolume(pid));
    }

    @PostMapping("/players/{pid}/volume")
    public ResponseEntity setPlayerVolume(@PathVariable("pid") final String pid,
                                          @RequestParam("volume") final int volume) {
        return ResponseEntity.ok(heosMusicService.setVolume(pid, volume));
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestHeader final String username,
                                 @RequestHeader final String password) {
        return ResponseEntity.ok(systemService.accountSignIn(username, password));
    }

    @GetMapping("/sources")
    public ResponseEntity getSources() {
        return ResponseEntity.ok(systemService.getMusicSources());
    }

    @Data
    public static class PlayerResp {
        private String pid;
        private String model;
        private String name;
        private Double gid;

        public PlayerResp(Player player) {
            this.pid = player.getPid();
            this.model = player.getModel();
            this.name = player.getName();
            this.gid = player.getGid();
        }
    }
}
