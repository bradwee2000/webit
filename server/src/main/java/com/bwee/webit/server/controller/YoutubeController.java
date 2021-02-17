package com.bwee.webit.server.controller;

import com.bwee.webit.model.YoutubeVideo;
import com.bwee.webit.service.YoutubeParser;
import com.bwee.webit.search.YoutubeEsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.bwee.webit.util.Constants.DEFAULT_PAGE_NUM;
import static com.bwee.webit.util.Constants.DEFAULT_PAGE_SIZE;

@Slf4j
@Controller
@RequestMapping("/youtube")
public class YoutubeController {

    @Autowired
    private YoutubeEsService youtubeEsService;

    @Autowired
    private YoutubeParser youtubeParser;

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable final String id) {
        return ResponseEntity.ok(youtubeEsService.findByIdStrict(id));
    }

    @GetMapping
    public ResponseEntity list(@RequestParam final Optional<Integer> page,
                               @RequestParam final Optional<Integer> size) {
        final Pageable pageable = PageRequest.of(page.orElse(DEFAULT_PAGE_NUM), size.orElse(DEFAULT_PAGE_SIZE));
        return ResponseEntity.ok(youtubeEsService.findAll(pageable).iterator());
    }

    @PostMapping
    public ResponseEntity save(@RequestBody final SaveYoutubeVideo req) {
        final Optional<YoutubeVideo> existing = youtubeEsService.findById(req.getId());
        final boolean isExist = existing.isPresent();
        final boolean isDownload = !isExist || req.isForceDownload();
        final boolean isIndex = !isExist || req.isForceIndex();

        log.info("Youtube id={}, exists={}, download={}, index={}", req.getId(), isExist, isDownload, isIndex);

        if (!isDownload && !isIndex) {
            return ResponseEntity.ok(existing.get());
        }

        final YoutubeVideo parsed = youtubeParser.parseVideo(req.getId(), isDownload);

        if (isIndex) {
            final YoutubeVideo saved = youtubeEsService.save(parsed);
            return ResponseEntity.ok(saved);
        }

        return ResponseEntity.ok(existing.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final String id) {
        youtubeEsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class SaveYoutubeVideo {
        private String id;
        private boolean forceDownload = false;
        private boolean forceIndex = false;
    }
}
