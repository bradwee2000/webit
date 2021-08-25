package com.bwee.webit.server.controller;

import com.bwee.webit.model.MusicUser;
import com.bwee.webit.stats.MusicStatsService;
import com.bwee.webit.service.MusicUserService;
import com.bwee.webit.service.PlayTrackCodeService;
import com.bwee.webit.service.TrackService;
import com.bwee.webit.server.exception.UnauthorizedAccessException;
import com.bwee.webit.server.service.MusicUserResFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    private PlayTrackCodeService playTrackCodeService;

    @Autowired
    private MusicUserService userService;

    @Autowired
    private MusicUserResFactory musicUserResFactory;

    @Autowired
    private HttpServletRequest req;

    @Value("${music.stream.security.enabled:true}")
    private Boolean isMusicStreamSecurityEnabled;

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable final String id) {
        return ResponseEntity.ok(trackService.findByIdStrict(id));
    }

    @PostMapping(value = "/{trackId}/play")
    public ResponseEntity play(@PathVariable final String trackId) {
        final MusicUser user = userService.playTrack(trackId);
        return ResponseEntity.ok(musicUserResFactory.build(user));
    }

    @SneakyThrows
    @GetMapping(value = "/{trackId}/stream", produces = "audio/mpeg")
    public ResponseEntity stream(@PathVariable final String trackId, @RequestParam("token") final String playToken) {
        if (isMusicStreamSecurityEnabled && !playTrackCodeService.isValid(trackId, playToken)) {
            throw new UnauthorizedAccessException(req);
        }
        final Path sourcePath = trackService.getSourcePathById(trackId);
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
