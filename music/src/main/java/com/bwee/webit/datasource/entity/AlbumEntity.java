package com.bwee.webit.datasource.entity;

import com.bwee.webit.model.Album;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.LIST;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.TEXT;

@Table("album")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AlbumEntity implements Entity<AlbumEntity> {

    public static AlbumEntity copyOf(final AlbumEntity e) {
        final AlbumEntity copy = new AlbumEntity();
        BeanUtils.copyProperties(e, copy);
        return copy;
    }

    @PrimaryKey
    private String id;

    private String name;

    private String imageUrl;

    private List<String> artists;

    private List<Track> tracks = emptyList();

    private int year;

    @CassandraType(type = LIST, typeArguments = TEXT)
    private List<String> tags = emptyList();

    public AlbumEntity(final Album album) {
        this.id = album.getId();
        this.name = album.getName();
        this.imageUrl = album.getImageUrl();
        this.artists = album.getArtists();
        this.tracks = album.getTracks().stream()
                .sorted(comparing(m -> m.getTrack()))
                .map(AlbumEntity.Track::new)
                .collect(toList());
        this.year = album.getYear();
        this.tags = album.getTags();
    }

    public List<String> getArtists() {
        return artists == null ? emptyList() : artists;
    }

    public List<String> getTags() {
        return tags == null ? emptyList() : tags;
    }

    public List<Track> getTracks() {
        return tracks == null ? emptyList() : tracks;
    }

    public Album toModel() {
        return new Album()
                .setId(id)
                .setName(name)
                .setImageUrl(imageUrl)
                .setArtists(getArtists())
                .setTracks(getTracks().stream().map(AlbumEntity.Track::toModel).collect(toList()))
                .setYear(year)
                .setTags(getTags());
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @UserDefinedType
    public static class Track {

        public static Track copyOf(final Track track) {
            final Track copy = new Track();
            BeanUtils.copyProperties(track, copy);
            return copy;
        }

        private String musicId;
        private Integer trackNum;

        public Track(String id, Integer track) {
            this.musicId = id;
            this.trackNum = track;
        }

        public Track(com.bwee.webit.model.Track track) {
            this.musicId = track.getId();
            this.trackNum = track.getTrack();
        }

        public com.bwee.webit.model.Track toModel() {
            return new com.bwee.webit.model.Track().setId(musicId).setTrack(trackNum);
        }
    }
}
