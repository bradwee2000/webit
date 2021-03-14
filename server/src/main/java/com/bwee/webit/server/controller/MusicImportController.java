package com.bwee.webit.server.controller;

import com.bwee.webit.model.Album;
import com.bwee.webit.server.model.music.ImportAlbumReq;
import com.bwee.webit.service.AlbumImporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.file.Path;

@Slf4j
@Controller
@RequestMapping("/music/import")
public class MusicImportController {

    @Autowired
    private AlbumImporter albumImporter;

    @Autowired
    private HttpServletRequest request;

    @PostMapping
    public ResponseEntity importAlbum(@RequestBody final ImportAlbumReq req) {
        final AlbumImporter.ImportConfig importConfig = AlbumImporter.ImportConfig.defaults()
                .path(Path.of(req.getPath()))
                .albumName(req.getName())
                .albumTags(req.getTags())
                .albumYear(req.getYear())
                .useFilenameAsTrackNum(req.isUserFilenameAsTrackNum())
                .copyFiles(req.isCopyFiles());

        log.info("Request: {} {}", req.getPath(), importConfig);

        final Album album = albumImporter.importAlbumFromPath(importConfig);

        final URI uri = UriComponentsBuilder.fromUriString(request.getRequestURI())
                .replacePath("/music/albums")
                .pathSegment(album.getId())
                .build()
                .toUri();
        return ResponseEntity.created(uri).body(album);
    }
}
