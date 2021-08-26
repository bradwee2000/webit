package com.bwee.webit.server.controller;

import com.bwee.webit.datasource.AlbumDbService;
import com.bwee.webit.datasource.TrackDbService;
import com.bwee.webit.model.Album;
import com.bwee.webit.model.Track;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/music/task")
public class MusicTaskController {

    @Autowired
    private TrackDbService dbService;

    @GetMapping
    public ResponseEntity fixImageUrls(@RequestParam("size") final int size,
                                       @RequestParam("page") final String page,
                                       @RequestParam("dryrun") final boolean isDryRun) {
        int count = 0;
        Slice<Track> tracks = dbService.findAll(CassandraPageRequest.first(100));

        dbService.saveAll(tracks.stream().map(t -> t.setImageUrl(fixUrl(t.getImageUrl()))).collect(Collectors.toList()));
        count+=tracks.getSize();

        while(tracks.hasNext()) {
            tracks = dbService.findAll(tracks.nextPageable());
            dbService.saveAll(tracks.stream().map(t -> t.setImageUrl(fixUrl(t.getImageUrl()))).collect(Collectors.toList()));
            count+=tracks.getSize();
        }
        return ResponseEntity.ok(count);
    }

    @SneakyThrows
    private String fixUrl(final String url) {
        return new URIBuilder(url).getPath();
    }
}
