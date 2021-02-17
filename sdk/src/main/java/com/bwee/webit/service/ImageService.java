package com.bwee.webit.service;

import com.bwee.webit.exception.ImageNotFoundException;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
public class ImageService {
    private static final String SERVICE_PATH = "images";
    private final String imageStorePath;
    private final String imageHost;
    private final IdGenerator idGenerator;

    @Getter
    public enum ImageType {
        JPEG("jpg");

        String ext;
        ImageType(String ext) {
            this.ext = ext;
        }
    }

    public ImageService(final String imageStorePath,
                        final String imageHost,
                        final IdGenerator idGenerator) {
        this.imageStorePath = imageStorePath;
        this.imageHost = imageHost;
        this.idGenerator = idGenerator;
    }

    @SneakyThrows
    public String put(final String bucket, final byte[] imageBytes) {
        return put(bucket, imageBytes, ImageType.JPEG);
    }

    @SneakyThrows
    public String put(final String bucket, final byte[] imageBytes, final ImageType imageType) {
        final String filename = idGenerator.generateId(imageBytes) + "." + imageType.getExt();
        final Path path = Path.of(imageStorePath, bucket, filename );

        Files.createDirectories(path.getParent());
        Files.write(path, imageBytes);

        return new URIBuilder(imageHost).setPathSegments(SERVICE_PATH, bucket, filename).build().toString();
    }

    @SneakyThrows
    public byte[] get(final String bucket, final String id) {
        final Path path = Path.of(imageStorePath, bucket, id);
        try {
            return Files.readAllBytes(path);
        } catch (final NoSuchFileException e) {
            throw new ImageNotFoundException(path.toString());
        }
    }
}
