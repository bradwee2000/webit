package com.bwee.webit.config;

import com.bwee.webit.heos.*;
import com.bwee.webit.heos.sddp.HeosClient;
import com.bwee.webit.heos.sddp.SsdpClient;
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

    @Test
    public void test1() {

    }

//    @Test
    @SneakyThrows
    public void test() {



        HeosSystem heos = new HeosSystem(heosClient);

        heos.accountSignIn("bradwee2000@gmail.com", "Sh@dow2355");

        Thread.sleep(1000);

        log.info("ACCOUNT: {}", heos.accountCheck());
        log.info("PLAYERS: {}", heos.getPlayers());
        Player player = heos.getPlayers().stream().findAny().get();

        log.info("NOW PLAYING: {}", playerService.getNowPlayingMedia(player.getPid()));
        log.info("VOLUME UP: {}", playerService.volumeUp(player.getPid(), 1));
        log.info("VOLUME UP: {}", playerService.volumeDown(player.getPid(), 1));

        for (MusicSource musicSource : heos.getMusicSources()) {
            log.info("MUSIC SOURCE: {}", musicSource);
        }

        final MusicSource local = heos.getMusicSources().stream().filter(s -> s.getSid().equals("5")).findFirst().orElse(null);
        log.info("BROWSE LOCAL: {}", local.browse());
    }

//    @Test
    @SneakyThrows
    public void test2() {

        final SsdpClient cl = new SsdpClient();

        String ip = cl.getHeosIp();

        HeosSystem sys = new HeosSystem(ip);

        System.out.println(sys.systemHeartBeat());

        log.info("PLAYERS: {}", sys.getPlayers());


        System.out.println(sys.getPlayers());

    }
}