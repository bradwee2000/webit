package com.bwee.webit.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @SneakyThrows
    public void copy(final Path from, final Path to) {
        Files.createDirectories(to.getParent());

        if (Files.exists(to)) {
            return;
        }

        Files.copy(from, to);
    }
}
