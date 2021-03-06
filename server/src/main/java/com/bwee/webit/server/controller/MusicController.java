package com.bwee.webit.server.controller;

import com.bwee.webit.model.Album;
import com.bwee.webit.server.auth.PlayTrackAuthService;
import com.bwee.webit.server.model.ImportAlbum;
import com.bwee.webit.server.model.music.PlayCodeRes;
import com.bwee.webit.service.Mp3Reader;
import com.bwee.webit.service.MusicAlbumImporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.file.Path;

@Slf4j
@Controller
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private MusicAlbumImporter musicAlbumImporter;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PlayTrackAuthService playTrackAuthService;

    @GetMapping("/play-code")
    public ResponseEntity getPlayCode() {
        return ResponseEntity.ok(new PlayCodeRes().setPlayCode(playTrackAuthService.getPlayCode()));
    }

    @PostMapping("/import")
    public ResponseEntity importAlbum(@RequestBody final ImportAlbum req) {
        final Mp3Reader.Config config = Mp3Reader.Config.defaults()
                .tags(req.getTags())
                .useFileNameAsTrackNum(req.isUserFilenameAsTrackNum());
        log.info("Request: {} {}", req.getPath(), config);

        final Album album = musicAlbumImporter.importAlbumFromPath(
                Path.of(req.getPath()),
                req.getName(),
                req.getTags(),
                req.getYear(),
                req.isUserFilenameAsTrackNum(),
                req.isCopyFiles());

        final URI uri = UriComponentsBuilder.fromUriString(request.getRequestURI())
                .replacePath("/music/albums")
                .pathSegment(album.getId())
                .build()
                .toUri();
        return ResponseEntity.created(uri).body(album);
    }
}
