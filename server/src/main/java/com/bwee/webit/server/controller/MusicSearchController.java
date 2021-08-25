package com.bwee.webit.server.controller;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.SearchType;
import com.bwee.webit.model.Track;
import com.bwee.webit.service.*;
import com.bwee.webit.server.model.music.SearchAlbumRes;
import com.bwee.webit.server.model.music.SearchAllMusicRes;
import com.bwee.webit.server.model.music.SearchTrackRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/music/search")
public class MusicSearchController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private ArtistService artistService;

    @GetMapping("/{query}")
    public ResponseEntity searchAll(@PathVariable final String query,
                                    @RequestParam final Optional<Integer> size,
                                    @RequestParam final Optional<Integer> page,
                                    @RequestParam("type") final Optional<String> type) {
        final Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(5));
        final SearchType searchType = type.map(SearchType::of).orElse(SearchType.allFields);

        final List<Track> tracks = trackService.search(query, searchType, pageable);
        final List<Album> albums = albumService.search(query, searchType, pageable);

        return ResponseEntity.ok(new SearchAllMusicRes().setTracks(tracks).setAlbums(albums));
    }

    @GetMapping("/{query}/albums")
    public ResponseEntity searchAlbums(@PathVariable final String query,
                                       @RequestParam final Optional<Integer> size,
                                       @RequestParam final Optional<Integer> page,
                                       @RequestParam("type") final Optional<String> type) {
        final Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(5));
        final SearchType searchType = type.map(SearchType::of).orElse(SearchType.allFields);

        final List<Album> albums = albumService.search(query, searchType, pageable);
        return ResponseEntity.ok(new SearchAlbumRes(albums));
    }

    @GetMapping("/{query}/tracks")
    public ResponseEntity searchTracks(@PathVariable final String query,
                                       @RequestParam final Optional<Integer> size,
                                       @RequestParam final Optional<Integer> page,
                                       @RequestParam("type") final Optional<String> type) {
        final Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(5));
        final SearchType searchType = type.map(SearchType::of).orElse(SearchType.allFields);

        final List<Track> tracks = trackService.search(query, searchType, pageable);
        return ResponseEntity.ok(new SearchTrackRes(tracks));
    }

    @GetMapping("/{query}/artists")
    public ResponseEntity searchArtists(@PathVariable final String query,
                                        @RequestParam final Optional<Integer> size,
                                        @RequestParam final Optional<Integer> page) {
        final Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(5));
        final List<String> artists = artistService.search(query, pageable);
        return ResponseEntity.ok(artists);
    }
}
