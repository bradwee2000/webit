package com.bwee.webit.config;

import com.bwee.webit.service.IdGenerator;
import com.bwee.webit.service.ImageService;
import com.bwee.webit.service.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfiguration {

    private static final int IMAGE_ID_SIZE = 20;

    @Value("${image.store.path:}")
    private String imageStorePath;

    @Value("${image.host:}")
    private String imageHost;

    @Bean
    public IdGenerator imageIdGenerator() {
        return new RandomIdGenerator(IMAGE_ID_SIZE);
    }

    @Bean
    public ImageService imageService() {
        return new ImageService(imageStorePath, imageHost, imageIdGenerator());
    }
}
