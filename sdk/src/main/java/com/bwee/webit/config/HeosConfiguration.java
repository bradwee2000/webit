package com.bwee.webit.config;

import com.bwee.webit.heos.HeosDiscoveryService;
import com.bwee.webit.heos.sddp.HeosClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class HeosConfiguration {

    @Bean
    public HeosClient heosClient(HeosDiscoveryService heosDiscoveryService) {
        return new HeosClient(() -> heosDiscoveryService.discoverHeosDeviceIp());
    }

//    @SneakyThrows
//    public Optional<InterfaceAddress> getBroadcast() {
//        Optional<InterfaceAddress> broadcast = NetworkInterface.networkInterfaces()
//                .filter(i -> {
//                    try {
//                        return !i.isLoopback();
//                    } catch (SocketException e) {
//                        return false;
//                    }
//                })
//                .flatMap(i -> i.getInterfaceAddresses().stream())
//                .filter(a -> a.getBroadcast() != null)
//                .findFirst();
//        return broadcast;
//    }

}
