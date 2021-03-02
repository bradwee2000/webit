package com.bwee.webit.core;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @SneakyThrows
    public void copy(final Path from, final Path to) {
        Files.createDirectories(to.getParent());
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
    }
}
