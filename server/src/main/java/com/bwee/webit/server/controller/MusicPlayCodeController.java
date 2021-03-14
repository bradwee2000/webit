package com.bwee.webit.server.controller;

import com.bwee.webit.service.PlayTrackCodeService;
import com.bwee.webit.server.model.music.PlayCodeRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/music/play-code")
public class MusicPlayCodeController {

    @Autowired
    private PlayTrackCodeService playTrackCodeService;

    @GetMapping
    public ResponseEntity getPlayCode() {
        return ResponseEntity.ok(new PlayCodeRes().setPlayCode(playTrackCodeService.getPlayCode()));
    }
}
