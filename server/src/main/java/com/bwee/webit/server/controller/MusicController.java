package com.bwee.webit.server.controller;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import com.bwee.webit.server.auth.PlayTrackAuthService;
import com.bwee.webit.server.model.ImportAlbum;
import com.bwee.webit.server.model.music.PlayCodeRes;
import com.bwee.webit.server.model.music.SearchAlbumResp;
import com.bwee.webit.server.model.music.SearchMusicAllResp;
import com.bwee.webit.server.model.music.SearchTrackResp;
import com.bwee.webit.service.AlbumService;
import com.bwee.webit.service.Mp3Reader;
import com.bwee.webit.service.MusicAlbumImporter;
import com.bwee.webit.service.TrackService;
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
import java.util.stream.Collectors;

import static com.bwee.webit.util.Constants.DEFAULT_PAGE_NUM;
import static com.bwee.webit.util.Constants.DEFAULT_PAGE_SIZE;

@Slf4j
@Controller
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicAlbumImporter musicAlbumImporter;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TrackService trackService;

    @Autowired
    private PlayTrackAuthService playTrackAuthService;

    @GetMapping("/search/{query}")
    public ResponseEntity searchAll(@PathVariable final String query) {
        final Pageable pageable = PageRequest.of(0, 5);
        final List<Track> tracks = trackService.search(query, pageable);
        final List<Album> albums = albumService.search(query, pageable);

        return ResponseEntity.ok(new SearchMusicAllResp().setTracks(tracks).setAlbums(albums));
    }

    @GetMapping("/search/{query}/tracks")
    public ResponseEntity searchTracks(@PathVariable final String query) {
        final List<Track> tracks = trackService.search(query);
        return ResponseEntity.ok(new SearchTrackResp(tracks));
    }

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam final String keywords,
                                 @RequestParam final Optional<Integer> page,
                                 @RequestParam final Optional<Integer> size) {
        final Pageable pageable = PageRequest.of(page.orElse(DEFAULT_PAGE_NUM), size.orElse(DEFAULT_PAGE_SIZE));

        final List<SearchAlbumResp> resp = albumService.search(keywords, pageable).stream()
                .map(SearchAlbumResp::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resp);
    }

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
