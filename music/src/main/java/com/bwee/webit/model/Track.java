package com.bwee.webit.model;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@Accessors(chain = true)
public class Track {

    private String id;
    private String trackNum;
    private String title;
    private String albumName;
    private String albumId;
    private String imageUrl;
    private String artist;
    private List<String> genre = Collections.emptyList();;
    private String originalArtist;
    private String composer;
    private String ext;
    private List<String> tags = Collections.emptyList();
    private long size;
    private long durationMillis;
    private int year;
    private int bitRate;
    private String channel;
    private int sampleRate;
    private Path sourcePath;
}
