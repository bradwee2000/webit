package com.bwee.webit.heos.config;

import com.bwee.webit.heos.client.MusicServiceClient;
import com.bwee.webit.heos.connect.HeosClient;
import com.bwee.webit.heos.service.HeosDiscoveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Slf4j
@Configuration
@EnableFeignClients(basePackageClasses = MusicServiceClient.class)
public class HeosConfiguration {

    @Bean
    public HeosClient heosClient(final HeosDiscoveryService heosDiscoveryService) {
        return new HeosClient(() -> heosDiscoveryService.discoverHeosDeviceIp(), Executors.newFixedThreadPool(3));
    }

//    @Bean(destroyMethod = "close")
//    public HeosListener heosListener(final HeosDiscoveryService heosDiscoveryService) {
//        return new HeosListener(() -> heosDiscoveryService.discoverHeosDeviceIp(), Executors.newFixedThreadPool(2));
//    }
}
