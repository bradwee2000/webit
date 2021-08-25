package com.bwee.webit.server.controller;

import com.bwee.webit.model.Album;
import com.bwee.webit.server.model.music.MusicRecommendationRes;
import com.bwee.webit.service.AlbumRecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import static com.bwee.webit.util.Constants.DEFAULT_PAGE_NUM;
import static com.bwee.webit.util.Constants.DEFAULT_PAGE_SIZE;

@Slf4j
@Controller
@RequestMapping("/music/recommendations")
public class MusicRecommendationController {

    @Autowired
    private AlbumRecommendationService albumRecommendationService;

    @GetMapping
    public ResponseEntity getRecommendations(@RequestParam final Optional<Integer> page,
                                             @RequestParam final Optional<Integer> size) {
        final Pageable pageable = PageRequest.of(page.orElse(DEFAULT_PAGE_NUM), size.orElse(DEFAULT_PAGE_SIZE));

        final List<Album> albumsToTry = albumRecommendationService.findAlbumsToTry(pageable);
        final List<Album> similarAlbums = albumRecommendationService.findAlbumsToTry(pageable);
        final List<Album> recentlyPlayedAlbums = albumRecommendationService.findAlbumsToTry(pageable);

        final MusicRecommendationRes res = new MusicRecommendationRes()
                .setAlbumsToTry(albumsToTry)
                .setRecentlyPlayed(recentlyPlayedAlbums)
                .setSimilarAlbums(similarAlbums);

        return ResponseEntity.ok(res);
    }
}
