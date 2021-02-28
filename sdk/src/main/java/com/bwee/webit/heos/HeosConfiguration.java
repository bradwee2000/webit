package com.bwee.webit.heos;

import com.bwee.webit.heos.sddp.HeosClient;
import com.bwee.webit.heos.sddp.HeosListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class HeosConfiguration {

    @Bean
    public HeosClient heosClient(final HeosDiscoveryService heosDiscoveryService) {
        return new HeosClient(() -> heosDiscoveryService.discoverHeosDeviceIp());
    }

    @Bean
    public HeosListener heosListener(final HeosDiscoveryService heosDiscoveryService) {
        return new HeosListener(() -> heosDiscoveryService.discoverHeosDeviceIp());
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
