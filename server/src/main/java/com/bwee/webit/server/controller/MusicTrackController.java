package com.bwee.webit.server.controller;

import com.bwee.webit.server.auth.PlayTrackAuthService;
import com.bwee.webit.service.TrackService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.Optional;

import static com.bwee.webit.util.Constants.DEFAULT_PAGE_NUM;
import static com.bwee.webit.util.Constants.DEFAULT_PAGE_SIZE;

@Slf4j
@Controller
@RequestMapping("/music/tracks")
public class MusicTrackController {

    @Autowired
    private TrackService trackService;

    @Autowired
    private PlayTrackAuthService playTrackAuthService;

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable final String id) {
        return ResponseEntity.ok(trackService.findByIdStrict(id));
    }

    @SneakyThrows
    @GetMapping(value = "/{id}/play", produces = "audio/mpeg")
    public ResponseEntity play(@PathVariable final String id, @RequestParam("token") final String playToken) {
        playTrackAuthService.validate(id, playToken);
        final Path sourcePath = trackService.getSourcePathById(id);
        return ResponseEntity.ok(new FileSystemResource(sourcePath));
    }

    @GetMapping
    public ResponseEntity list(@RequestParam final Optional<Integer> page,
                               @RequestParam final Optional<Integer> size) {
        final Pageable pageable = PageRequest.of(page.orElse(DEFAULT_PAGE_NUM), size.orElse(DEFAULT_PAGE_SIZE));
        return ResponseEntity.ok(trackService.findAll(pageable).iterator());
    }

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam final String keywords,
                                 @RequestParam final Optional<Integer> page,
                                 @RequestParam final Optional<Integer> size) {
        final Pageable pageable = PageRequest.of(page.orElse(DEFAULT_PAGE_NUM), size.orElse(DEFAULT_PAGE_SIZE));
        return ResponseEntity.ok(trackService.search(keywords, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final String id) {
        trackService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
