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
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.*;

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

    @CassandraType(type = LIST, typeArguments = TEXT)
    private List<String> artists;

    private Integer year;

    @CassandraType(type = SET, typeArguments = TEXT)
    private List<String> tags = emptyList();

    public AlbumEntity(final Album album) {
        this.id = album.getId();
        this.name = album.getName();
        this.imageUrl = album.getImageUrl();
        this.artists = album.getArtists();
        this.year = album.getYear();
        this.tags = album.getTags();
    }

    public Album toModel() {
        return new Album()
                .setId(id)
                .setName(name)
                .setImageUrl(imageUrl)
                .setArtists(artists == null ? emptyList() : artists)
                .setYear(year)
                .setTags(tags == null ? emptyList() : tags);
    }
}
