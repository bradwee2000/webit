package com.bwee.webit.server.controller;

import com.bwee.webit.model.MusicUser;
import com.bwee.webit.model.Track;
import com.bwee.webit.server.model.music.SaveMusicUserReq;
import com.bwee.webit.server.model.music.UpdateQueueReq;
import com.bwee.webit.server.service.MusicUserResFactory;
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

    @Autowired
    private MusicUserResFactory musicUserResFactory;

    @GetMapping
    public ResponseEntity getMusicUser() {
        final MusicUser user = userService.getLoginUser();
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity saveMusicUser(@RequestBody final SaveMusicUserReq req) {
        final MusicUser user = new MusicUser().setId(req.getId());
        userService.insertNewUser(user);
        return ResponseEntity.ok(musicUserResFactory.build(user));
    }

    @PostMapping("/shuffle")
    public ResponseEntity shuffle(@RequestParam(value = "isShuffle", defaultValue = "true") final Boolean isShuffle) {
        final MusicUser user = userService.updateShuffle(isShuffle);
        return ResponseEntity.ok(musicUserResFactory.build(user));
    }

    @PostMapping("/loop")
    public ResponseEntity loop(@RequestParam(value = "isLoop", defaultValue = "true") final Boolean isLoop) {
        final MusicUser user = userService.updateLoop(isLoop);
        return ResponseEntity.ok(musicUserResFactory.build(user));
    }

    @PostMapping("/next")
    public ResponseEntity nextTrack() {
        final MusicUser user = userService.nextTrack();
        return ResponseEntity.ok(musicUserResFactory.build(user));
    }

    @PostMapping("/prev")
    public ResponseEntity prevTrack() {
        final MusicUser user = userService.prevTrack();
        return ResponseEntity.ok(musicUserResFactory.build(user));
    }
}