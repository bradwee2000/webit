package com.bwee.webit.server.model.music;

import com.bwee.webit.model.Album;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Data
@NoArgsConstructor
public class SearchAlbumRes {

    private List<AlbumRes> albums;

    public SearchAlbumRes(final List<Album> albums) {
        this.albums = albums.stream().map(AlbumRes::new).collect(Collectors.toList());
    }
}
