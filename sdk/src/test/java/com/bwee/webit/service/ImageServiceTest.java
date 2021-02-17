package com.bwee.webit.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ImageServiceTest {

    private final String imageHost = "https://img.com";

    private IdGenerator idGenerator;
    private ImageService imageService;

    @SneakyThrows
    @BeforeEach
    public void before() {
        idGenerator = mock(IdGenerator.class);
        when(idGenerator.generateId(any())).thenReturn("A", "B", "C");

        final Path tempDirPath = Files.createTempDirectory("webit-");
        imageService = new ImageService(tempDirPath.toString(), imageHost, idGenerator);
    }

    @Test
    public void testPut_shouldReturnUrl() {
        final String url1 = imageService.put("sample", "test image 1".getBytes());
        final String url2 = imageService.put("sample", "test image 2".getBytes());
        assertThat(url1).isEqualTo("https://img.com/images/sample/A.jpg");
        assertThat(url2).isEqualTo("https://img.com/images/sample/B.jpg");
    }


    @Test
    public void testGet_shouldReturnBytes() {
        imageService.put("sample", "Hello World".getBytes());
        assertThat(imageService.get("sample", "A.jpg")).isEqualTo("Hello World".getBytes());
    }
}