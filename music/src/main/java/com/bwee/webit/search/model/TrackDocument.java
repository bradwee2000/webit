package com.bwee.webit.search.model;

import com.bwee.webit.model.Track;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Collections;
import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Data
@Accessors(chain = true)
@Document(indexName = "track")
@NoArgsConstructor
public class TrackDocument implements SearchDocument<Track> {

    @Id
    private String id;

    @Field(type = Text)
    private String title;

    @Field(type = Text)
    private String albumName;

    @Field(type = Text)
    private String artist;

    @Field(type = Text)
    private String originalArtist;

    @Field(type = Text)
    private String composer;

    @Field(type = Text)
    private List<String> genre;

    @Field(type = Keyword)
    private List<String> tags = Collections.emptyList();

    @Field(type = FieldType.Long)
    private Long duration;

    @Field(type = Keyword)
    private Integer year;

    public TrackDocument(final Track track) {
        this.id = track.getId();
        this.title = track.getTitle();
        this.albumName = track.getAlbumName();
        this.artist = track.getArtist();
        this.composer = track.getComposer();
        this.genre = track.getGenre();
        this.originalArtist = track.getOriginalArtist();
        this.duration = track.getDurationMillis();
        this.year = track.getYear();
        this.tags = track.getTags();
    }

    @Override
    public Track toModel() {
        return new Track().setId(id)
                .setTitle(title)
                .setAlbumName(albumName)
                .setArtist(artist)
                .setComposer(composer)
                .setGenre(genre)
                .setTags(tags)
                .setOriginalArtist(originalArtist)
                .setDurationMillis(duration)
                .setYear(year);
    }
}
