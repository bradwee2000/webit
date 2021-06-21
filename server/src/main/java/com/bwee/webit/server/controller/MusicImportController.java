package com.bwee.webit.server.controller;

import com.bwee.webit.model.Album;
import com.bwee.webit.server.model.music.ImportAlbumReq;
import com.bwee.webit.service.AlbumImporter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequestMapping("/music/import")
public class MusicImportController {

    @Autowired
    private AlbumImporter albumImporter;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public ResponseEntity listUnprocessedMusicPaths() {
        return listUnprocessedMusicSubPaths(null);
    }

    @GetMapping("/{subPath}")
    public ResponseEntity listUnprocessedMusicSubPaths(@PathVariable(value = "subPath", required = false) final String subPath) {
        log.info("Sub-Path: {}", subPath);
        final List<MusicPath> paths = albumImporter.listUnprocessedMusicPaths(subPath).stream()
                .map(p -> new MusicPath()
                        .setPath(p.toAbsolutePath().toString())
                        .setDir(p.toFile().isDirectory())
                        .setFilename(p.getFileName().toString()))
                .collect(toList());
        return ResponseEntity.ok(paths);
    }

    @PostMapping("/all")
    public ResponseEntity importAllAlbums() {
        final List<Album> albums = albumImporter.listUnprocessedMusicPaths().stream()
                .map(path -> AlbumImporter.ImportConfig.defaults().path(path))
                .map(config -> {
                    try {
                        return albumImporter.importAlbumFromPath(config);
                    } catch (final Exception e) {
                        log.warn("Skipping album {}. Error: {}", config.path(), e.getMessage());
                        return null;
                    }
                })
                .filter(album -> album != null)
                .filter(album -> !album.getTracks().isEmpty())
                .collect(toList());

        return ResponseEntity.ok(albums);
    }

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

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MusicPath {
        private String path;
        private boolean isDir = false;
        private String filename;
    }
}
