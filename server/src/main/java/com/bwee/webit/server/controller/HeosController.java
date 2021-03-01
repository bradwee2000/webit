package com.bwee.webit.server.controller;

import com.bwee.webit.heos.HeosBrowseService;
import com.bwee.webit.heos.HeosPlayerService;
import com.bwee.webit.heos.HeosSystemService;
import com.bwee.webit.heos.model.Player;
import com.bwee.webit.heos.connect.HeosClient;
import com.bwee.webit.heos.connect.HeosListener;
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
    private HeosClient heosClient;

    @Autowired
    private HeosListener heosListener;

    @PostMapping("/connect")
    public ResponseEntity connect() {
        heosClient.connect();
        heosListener.startListening();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/players")
    public ResponseEntity getPlayers() {
        return ResponseEntity.ok(systemService.getPlayers().stream().map(PlayerResp::new).collect(Collectors.toList()));
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
        return ResponseEntity.ok(playerService.getVolume(pid));
    }

    @PostMapping("/players/{pid}/volume")
    public ResponseEntity setPlayerVolume(@PathVariable("pid") final String pid,
                                          @RequestParam("volume") final int volume) {
        return ResponseEntity.ok(playerService.setVolume(pid, volume));
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
