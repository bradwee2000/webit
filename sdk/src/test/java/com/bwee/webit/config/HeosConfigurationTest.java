package com.bwee.webit.config;

import com.bwee.webit.heos.*;
import com.bwee.webit.heos.sddp.HeosClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@Import({HeosConfiguration.class,
        HeosSystemService.class,
        HeosPlayerService.class,
        HeosDiscoveryService.class})
public class HeosConfigurationTest {

    @Autowired
    private HeosConfiguration config;

    @Autowired
    private HeosClient heosClient;

    @Autowired
    private HeosPlayerService playerService;

    @Autowired
    private HeosSystemService systemService;

    @Test
    public void test1() {

    }

    @Test
    @SneakyThrows
    public void test() {

        systemService.accountSignIn("bradwee2000@gmail.com", "Sh@dow2355");

        Thread.sleep(1000);

        log.info("ACCOUNT: {}", systemService.accountCheck());
        log.info("PLAYERS: {}", systemService.getPlayers());
        Player player = systemService.getPlayers().stream().findAny().get();

        log.info("NOW PLAYING: {}", playerService.getNowPlayingMedia(player.getPid()));
        log.info("VOLUME UP: {}", playerService.volumeUp(player.getPid(), 1));
        log.info("VOLUME UP: {}", playerService.volumeDown(player.getPid(), 1));
        playerService.setPlayState(player.getPid(), PlayState.PLAY);

        playerService.playUrl(player.getPid(), "http://localhost:8080/music/tracks/XQ89mt0iLKGV5JEcILi6zIgC-lw=/play");

        for (MusicSource musicSource : systemService.getMusicSources()) {
            log.info("MUSIC SOURCE: {}", musicSource);
        }

        final MusicSource local = systemService.getMusicSources().stream().filter(s -> s.getSid().equals("5")).findFirst().orElse(null);
//        log.info("BROWSE LOCAL: {}", local.browse());
    }
}