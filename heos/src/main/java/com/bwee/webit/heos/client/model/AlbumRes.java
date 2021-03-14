package com.bwee.webit.heos.client.model;

import lombok.Data;

import java.util.List;

@Data
public class AlbumRes {
    private final String id;
    private final String name;
    private final List<String> artists;
    private final int year;
    private final String imageUrl;
    private final List<String> tags;
}
