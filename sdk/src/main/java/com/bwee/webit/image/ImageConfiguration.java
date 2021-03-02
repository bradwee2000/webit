package com.bwee.webit.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfiguration {

    @Value("${image.store.path:}")
    private String imageStorePath;

    @Value("${image.host:}")
    private String imageHost;

    @Bean
    public ImageService imageService(ImageIdGenerator idGenerator) {
        return new ImageService(imageStorePath, imageHost, idGenerator);
    }
}
