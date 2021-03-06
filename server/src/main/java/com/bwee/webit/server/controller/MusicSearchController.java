package com.bwee.webit.server.controller;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import com.bwee.webit.server.auth.PlayTrackAuthService;
import com.bwee.webit.server.model.music.SearchAlbumRes;
import com.bwee.webit.server.model.music.SearchAllMusicRes;
import com.bwee.webit.server.model.music.SearchTrackRes;
import com.bwee.webit.service.AlbumService;
import com.bwee.webit.service.MusicAlbumImporter;
import com.bwee.webit.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bwee.webit.util.Constants.DEFAULT_PAGE_NUM;
import static com.bwee.webit.util.Constants.DEFAULT_PAGE_SIZE;

@Slf4j
@Controller
@RequestMapping("/music/search")
public class MusicSearchController {

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

    @GetMapping("/{query}")
    public ResponseEntity searchAll(@PathVariable final String query) {
        final Pageable pageable = PageRequest.of(0, 5);
        final List<Track> tracks = trackService.search(query, pageable);
        final List<Album> albums = albumService.search(query, pageable);

        return ResponseEntity.ok(new SearchAllMusicRes().setTracks(tracks).setAlbums(albums));
    }

    @GetMapping("/{query}/albums")
    public ResponseEntity searchAlbums(@PathVariable final String query) {
        final List<Album> albums = albumService.search(query);
        return ResponseEntity.ok(new SearchAlbumRes(albums));
    }

    @GetMapping("/{query}/tracks")
    public ResponseEntity searchTracks(@PathVariable final String query) {
        final List<Track> tracks = trackService.search(query);
        return ResponseEntity.ok(new SearchTrackRes(tracks));
    }
}
