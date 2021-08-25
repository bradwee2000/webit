package com.bwee.webit.search.model;

import com.bwee.webit.model.Album;
import com.bwee.webit.util.MusicUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Data
@Slf4j
@Accessors(chain = true)
@Document(indexName = "album")
@NoArgsConstructor
public class AlbumDocument implements SearchDocument<Album> {

    public static AlbumDocument copyOf(final AlbumDocument doc) {
        final AlbumDocument copy = new AlbumDocument();
        BeanUtils.copyProperties(doc, copy);
        return copy;
    }

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private List<String> artists = emptyList();

    @Field(type = FieldType.Text)
    private List<String> trackNames = emptyList();

    @Field(type = FieldType.Keyword)
    private List<String> tags = emptyList();

    @Field(type = FieldType.Keyword)
    private Integer year;

    public AlbumDocument(final Album album) {
        this.id = album.getId();
        this.name = album.getDisplayName();
        this.artists = album.getArtists();
        this.year = album.getYear();
        this.tags = album.getTags();
        this.artists = album.getArtists();
        this.trackNames = album.getTracks().stream()
                .map(m -> MusicUtils.joinArtistsToString(m.getArtists()) + " " + m.getTitle())
                .collect(Collectors.toList());
    }

    @Override
    public Album toModel() {
        return new Album().setId(id)
                .setDisplayName(name)
                .setArtists(artists == null ? emptyList() : artists)
                .setTags(tags == null ? emptyList() : tags)
                .setYear(year);
    }
}
