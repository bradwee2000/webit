package com.bwee.webit.server.controller;

import com.bwee.webit.model.Album;
import com.bwee.webit.server.model.ImportAlbum;
import com.bwee.webit.server.model.music.SearchAlbumResp;
import com.bwee.webit.service.AlbumService;
import com.bwee.webit.service.Mp3Reader;
import com.bwee.webit.service.MusicAlbumImporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static com.bwee.webit.util.Constants.DEFAULT_PAGE_NUM;
import static com.bwee.webit.util.Constants.DEFAULT_PAGE_SIZE;
import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequestMapping("/music/albums")
public class MusicAlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicAlbumImporter musicAlbumImporter;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable final String id) {
        return ResponseEntity.ok(albumService.findByIdStrict(id));
    }

    @GetMapping
    public ResponseEntity list(@RequestParam final Optional<Integer> page,
                               @RequestParam final Optional<Integer> size) {
        final Pageable pageable = PageRequest.of(page.orElse(DEFAULT_PAGE_NUM), size.orElse(DEFAULT_PAGE_SIZE));
        return ResponseEntity.ok(albumService.findAll(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam final String keywords,
                                 @RequestParam final Optional<Integer> page,
                                 @RequestParam final Optional<Integer> size) {
        final Pageable pageable = PageRequest.of(page.orElse(DEFAULT_PAGE_NUM), size.orElse(DEFAULT_PAGE_SIZE));

        final List<SearchAlbumResp> resp = albumService.search(keywords, pageable).stream()
                .map(SearchAlbumResp::new)
                .collect(toList());

        return ResponseEntity.ok(resp);
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

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final String id) {
        albumService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
