package com.bwee.webit.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneOffset;

@Configuration
public class SystemConfiguration {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneOffset.UTC);
    }
}
