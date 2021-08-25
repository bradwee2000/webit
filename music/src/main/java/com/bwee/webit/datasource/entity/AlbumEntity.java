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
public class AlbumEntity implements Entity<AlbumEntity, String> {

    public static AlbumEntity copyOf(final AlbumEntity e) {
        final AlbumEntity copy = new AlbumEntity();
        BeanUtils.copyProperties(e, copy);
        return copy;
    }

    @PrimaryKey
    private String id;

    private String originalName;

    private String displayName;

    private String imageUrl;

    @CassandraType(type = LIST, typeArguments = TEXT)
    private List<String> artists;

    private Integer year;

    private String sourcePath;

    @CassandraType(type = SET, typeArguments = TEXT)
    private List<String> tags = emptyList();

    public AlbumEntity(final Album album) {
        this.id = album.getId();
        this.originalName = album.getOriginalName();
        this.displayName = album.getDisplayName();
        this.imageUrl = album.getImageUrl();
        this.artists = album.getArtists();
        this.year = album.getYear();
        this.tags = album.getTags();
        this.sourcePath = album.getSourcePath();
    }

    public Album toModel() {
        return new Album()
                .setId(id)
                .setOriginalName(originalName)
                .setDisplayName(displayName)
                .setImageUrl(imageUrl)
                .setArtists(artists == null ? emptyList() : artists)
                .setYear(year)
                .setTags(tags == null ? emptyList() : tags)
                .setSourcePath(sourcePath);
    }
}
