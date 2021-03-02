package com.bwee.webit.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;

@Configuration
public class SystemConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
