package com.bwee.webit.server.model.music;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class AlbumTracksRes {
    private String id;
    private String name;
    private List<String> artists;
    private List<TrackRes> tracks;
    private Integer year;
    private String imageUrl;
    private List<String> tags;
}
