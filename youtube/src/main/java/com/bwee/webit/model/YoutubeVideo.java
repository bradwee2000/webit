package com.bwee.webit.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Data
@Accessors(chain = true)
public class YoutubeVideo {

    private String id;
    private String title;
    private String description;
    private String sourceUrl;
    private String author;
    private String ext;
    private List<String> categories = Collections.emptyList();
    private List<String> tags = Collections.emptyList();
    private Long duration;
    private Long width;
    private Long height;
    private Long size;
    private Long timestamp;
}
