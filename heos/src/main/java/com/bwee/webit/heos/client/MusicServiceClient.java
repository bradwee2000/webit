package com.bwee.webit.heos.client;

import com.bwee.webit.heos.client.model.MusicUserRes;
import com.bwee.webit.heos.client.model.PlayCodeRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "MusicClient", url="http://localhost:8080/music")
public interface MusicServiceClient {

    @PostMapping(value = "/user/next")
    MusicUserRes playNext(@RequestHeader("Authorization") final String token);

    @PostMapping(value = "/user/prev")
    MusicUserRes playPrev(@RequestHeader("Authorization") final String token);

    @GetMapping(value = "/user")
    MusicUserRes get(@RequestHeader("Authorization") final String token);

    @GetMapping(value = "/play-code")
    PlayCodeRes getPlayCode(@RequestHeader("Authorization") final String token);
}
