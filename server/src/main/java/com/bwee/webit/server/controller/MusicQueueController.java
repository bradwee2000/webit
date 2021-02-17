package com.bwee.webit.server.controller;

import com.bwee.webit.auth.AuthenticationService;
import com.bwee.webit.heos.sddp.Response;
import com.bwee.webit.model.MusicUser;
import com.bwee.webit.model.Track;
import com.bwee.webit.service.MusicUserService;
import com.bwee.webit.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity list() {
        final List<Track> tracks = userService.getQueue();
        return ResponseEntity.ok(tracks);
    }

    @PostMapping("/next")
    public ResponseEntity next() {
        return ResponseEntity.ok("");
    }

    @PostMapping("/prev")
    public ResponseEntity prev() {
        return ResponseEntity.ok("");
    }

    @PostMapping
    public ResponseEntity save() {

        return ResponseEntity.ok("");
    }

    @PostMapping("/shuffle")
    public ResponseEntity shuffle() {
        return ResponseEntity.ok("");
    }

    @PostMapping("/loop")
    public ResponseEntity loop() {
        return ResponseEntity.ok("");
    }
}
