package com.bwee.webit.model;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

@Data
@Accessors(chain = true)
public class Track {

    private String id;
    private String trackNum;
    private String title;
    private String albumName;
    private String albumId;
    private String imageUrl;
    private List<String> artists = emptyList();
    private List<String> genre = emptyList();;
    private String originalAlbumName; // Album name as found in MP3 file. Doesn't change.
    private String originalArtist;
    private String composer;
    private String ext;
    private List<String> tags = emptyList();
    private long size;
    private long durationMillis;
    private Integer year;
    private Integer bitRate;
    private String channel;
    private Integer sampleRate;
    private Path sourcePath;
}
