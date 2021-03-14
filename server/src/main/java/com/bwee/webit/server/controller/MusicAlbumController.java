package com.bwee.webit.server.controller;

import com.bwee.webit.model.Album;
import com.bwee.webit.model.MusicUser;
import com.bwee.webit.server.service.AlbumTrackResFactory;
import com.bwee.webit.server.service.MusicUserResFactory;
import com.bwee.webit.service.AlbumService;
import com.bwee.webit.service.MusicUserService;
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
@RequestMapping("/music/albums")
public class MusicAlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicUserService userService;

    @Autowired
    private MusicUserResFactory musicUserResFactory;

    @Autowired
    private AlbumTrackResFactory albumTrackResFactory;

    @GetMapping("/{albumId}")
    public ResponseEntity get(@PathVariable final String albumId) {
        final Album album = albumService.findByIdStrict(albumId);
        return ResponseEntity.ok(albumTrackResFactory.build(album));
    }

    @PostMapping(value = "/{albumId}/play")
    public ResponseEntity play(@PathVariable final String albumId) {
        final Album album = albumService.findByIdStrict(albumId);
        final MusicUser user = userService.playAlbum(album);
        return ResponseEntity.ok(musicUserResFactory.build(user, album));
    }

    @GetMapping
    public ResponseEntity list(@RequestParam final Optional<Integer> page,
                               @RequestParam final Optional<Integer> size) {
        final Pageable pageable = PageRequest.of(page.orElse(DEFAULT_PAGE_NUM), size.orElse(DEFAULT_PAGE_SIZE));
        return ResponseEntity.ok(albumService.findAll(pageable).getContent());
    }

    @DeleteMapping("/{albumId}")
    public ResponseEntity delete(@PathVariable final String albumId) {
        albumService.deleteById(albumId);
        return ResponseEntity.noContent().build();
    }
}
