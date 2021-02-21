package com.bwee.webit.server.controller;

import com.bwee.webit.auth.AuthenticationService;
import com.bwee.webit.heos.sddp.Response;
import com.bwee.webit.model.MusicUser;
import com.bwee.webit.model.Track;
import com.bwee.webit.server.model.music.UpdateLoopReq;
import com.bwee.webit.server.model.music.UpdateQueueReq;
import com.bwee.webit.server.model.music.UpdateShuffleReq;
import com.bwee.webit.service.MusicUserService;
import com.bwee.webit.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/music/queue")
public class MusicQueueController {

    @Autowired
    private TrackService trackService;

    @Autowired
    private MusicUserService userService;

    @GetMapping
    public ResponseEntity getTracks() {
        final List<Track> tracks = userService.getQueue();
        return ResponseEntity.ok(tracks);
    }

    @PostMapping("/next")
    public ResponseEntity next() {
        final Track track = userService.nextTrack().orElse(null);
        return ResponseEntity.ok(track);
    }

    @PostMapping("/prev")
    public ResponseEntity prev() {
        final Track track = userService.prevTrack().orElse(null);
        return ResponseEntity.ok(track);
    }

    @PostMapping
    public ResponseEntity saveQueue(@RequestBody final UpdateQueueReq req) {
        final MusicUser user = userService.updateTrackQueue(req.getTrackIds());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/shuffle")
    public ResponseEntity shuffle(@RequestBody final UpdateShuffleReq req) {
        final MusicUser user = userService.updateShuffle(req.isEnabled());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/loop")
    public ResponseEntity loop(@RequestBody final UpdateLoopReq req) {
        final MusicUser user = userService.updateLoop(req.getIsEnabled());
        return ResponseEntity.ok(user);
    }
}
