package com.bwee.webit.server.controller;

import com.bwee.webit.model.MusicUser;
import com.bwee.webit.model.Track;
import com.bwee.webit.server.model.music.SaveMusicUserReq;
import com.bwee.webit.server.model.music.UpdateQueueReq;
import com.bwee.webit.service.MusicUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/music/user")
public class MusicUserController {

    @Autowired
    private MusicUserService userService;

    @GetMapping
    public ResponseEntity getMusicUser() {
        final MusicUser user = userService.getLoginUser();
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity saveMusicUser(@RequestBody SaveMusicUserReq req) {
        final MusicUser user = new MusicUser().setName(req.getName());
        userService.insertNewUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/shuffle")
    public ResponseEntity shuffle(@RequestParam("isShuffle") Boolean isShuffle) {
        final MusicUser user = userService.updateShuffle(isShuffle);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/loop")
    public ResponseEntity loop(@RequestParam("isLoop") Boolean isLoop) {
        final MusicUser user = userService.updateLoop(isLoop);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/next")
    public ResponseEntity nextTrack() {
        final Optional<Track> track = userService.nextTrack();
        return ResponseEntity.ok(track.orElse(null));
    }

    @PostMapping("/prev")
    public ResponseEntity prevTrack() {
        final Optional<Track> track = userService.prevTrack();
        return ResponseEntity.ok(track.orElse(null));
    }

    @PostMapping("/queue")
    public ResponseEntity updateQueue(@RequestBody final UpdateQueueReq req) {
        final MusicUser user = userService.updateTrackQueue(req.getTrackIds());
        return ResponseEntity.ok(user);
    }
}