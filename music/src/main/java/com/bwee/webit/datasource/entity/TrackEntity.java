package com.bwee.webit.datasource.entity;

import com.bwee.webit.model.Track;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.LIST;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.TEXT;

@Table("track")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TrackEntity implements Entity<TrackEntity, String> {

    public static TrackEntity copyOf(final TrackEntity e) {
        final TrackEntity copy = new TrackEntity();
        BeanUtils.copyProperties(e, copy);
        return copy;
    }

    @PrimaryKey
    private String id;

    @Indexed
    private String albumId;

    private String title;
    private String albumName;
    private String originalAlbumName;
    private String originalArtist;
    private String composer;
    private String trackNum;

    @CassandraType(type = LIST, typeArguments = TEXT)
    private List<String> artists;

    @CassandraType(type = LIST, typeArguments = TEXT)
    private List<String> genre = emptyList();

    @CassandraType(type = LIST, typeArguments = TEXT)
    private List<String> tags = emptyList();

    private String ext;
    private Long size;
    private Long durationMillis;
    private Integer year;
    private Integer bitRate;
    private String channel;
    private Integer sampleRate;
    private String imageUrl;

    public TrackEntity(final Track track) {
        this.id = track.getId();
        this.title = track.getTitle();
        this.albumId = track.getAlbumId();
        this.trackNum = track.getTrackNum();
        this.albumName = track.getAlbumName();
        this.originalAlbumName = track.getOriginalAlbumName();
        this.artists = track.getArtists();
        this.originalArtist = track.getOriginalArtist();
        this.composer = track.getComposer();
        this.genre = track.getGenre();
        this.size = track.getSize();
        this.durationMillis = track.getDurationMillis();
        this.year = track.getYear();
        this.bitRate = track.getBitRate();
        this.channel = track.getChannel();
        this.sampleRate = track.getSampleRate();
        this.tags = track.getTags();
        this.ext = track.getExt();
        this.imageUrl = track.getImageUrl();
    }

    public Track toModel() {
        return new Track()
                .setId(id)
                .setTitle(title)
                .setTrackNum(trackNum)
                .setAlbumId(albumId)
                .setAlbumName(albumName)
                .setArtists(artists == null ? emptyList() : artists)
                .setOriginalAlbumName(originalAlbumName)
                .setOriginalArtist(originalArtist)
                .setComposer(composer)
                .setGenre(genre == null ? emptyList() : genre)
                .setSize(size)
                .setDurationMillis(durationMillis)
                .setYear(year)
                .setBitRate(bitRate)
                .setChannel(channel)
                .setSampleRate(sampleRate)
                .setExt(ext)
                .setImageUrl(imageUrl)
                .setTags(tags == null ? emptyList() : tags);
    }
}
