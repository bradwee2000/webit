package com.bwee.webit.server.controller;

import com.bwee.webit.auth.AuthenticationService;
import com.bwee.webit.datasource.MusicUserDbService;
import com.bwee.webit.model.Album;
import com.bwee.webit.model.MusicUser;
import com.bwee.webit.model.Track;
import com.bwee.webit.server.model.ImportAlbum;
import com.bwee.webit.server.model.music.QueueUpdateReq;
import com.bwee.webit.server.model.music.SearchAlbumResp;
import com.bwee.webit.server.model.music.SearchMusicAllResp;
import com.bwee.webit.server.model.music.SearchTrackResp;
import com.bwee.webit.service.*;
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
    private AuthenticationService authenticationService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicAlbumImporter musicAlbumImporter;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TrackService trackService;

    @Autowired
    private MusicUserService userService;

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

    @PostMapping("/shuffle")
    public ResponseEntity shuffle(@RequestParam("isShuffle") Boolean isShuffle) {
        final MusicUser user = userService.updateShuffle(isShuffle);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/loop")
    public ResponseEntity loop(@RequestParam("isLoop") Boolean isLoop) {
        final MusicUser user = userService.updateLoop(isLoop);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/next")
    public ResponseEntity nextTrack() {
        final Optional<Track> track = userService.nextTrack();
        return ResponseEntity.ok(track.orElse(null));
    }

    @PostMapping("/prev")
    public ResponseEntity prevTrack() {
        final Optional<Track> track = userService.prevTrack();
        return ResponseEntity.ok(track.orElse(null));
    }

    @GetMapping("/user")
    public ResponseEntity getMusicUser() {
        final MusicUser user = userService.getLoginUser();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/queue")
    public ResponseEntity queue(@RequestBody QueueUpdateReq req) {
        final MusicUser user = userService.updateTrackQueue(req.getTrackIds());
        return ResponseEntity.ok(user);
    }
}
