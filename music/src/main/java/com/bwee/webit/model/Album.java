package com.bwee.webit.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import static java.util.Collections.emptyList;

@Data
@Accessors(chain = true)
public class Album {

    private String id;
    private String name;
    private String imageUrl;
    private List<String> artists = emptyList();
    private List<Track> tracks = emptyList();
    private int year;
    private List<String> tags = emptyList();
}
