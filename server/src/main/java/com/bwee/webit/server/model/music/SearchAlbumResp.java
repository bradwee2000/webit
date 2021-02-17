package com.bwee.webit.server.model.music;

import com.bwee.webit.model.Album;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.Collections.emptyList;

@Data
@NoArgsConstructor
public class SearchAlbumResp {

    private String id;
    private String name;
    private List<String> artists = emptyList();
    private int year;
    private int trackSize;
    private List<String> tags = emptyList();

    public SearchAlbumResp(final Album album) {
        this.id = album.getId();
        this.name = album.getName();
        this.artists = album.getArtists();
        this.year = album.getYear();
        this.trackSize = album.getTracks().size();
        this.tags = album.getTags();
    }
}
