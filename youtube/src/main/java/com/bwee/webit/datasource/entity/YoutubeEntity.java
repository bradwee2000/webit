package com.bwee.webit.datasource.entity;

import com.bwee.webit.model.YoutubeVideo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Table("youtube")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class YoutubeEntity implements Entity<YoutubeEntity, String> {

    @PrimaryKey
    private String id;
    private String title;
    private String description;
    private String sourceUrl;
    private String author;
    private String ext;
    private List<String> categories;
    private List<String> tags;
    private Long duration;
    private Long width;
    private Long height;
    private Long size;
    private Long timestamp;

    public YoutubeEntity(final YoutubeVideo video) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.description = video.getDescription();
        this.sourceUrl = video.getSourceUrl();
        this.author = video.getAuthor();
        this.ext = video.getExt();
        this.categories = video.getCategories();
        this.tags = video.getTags();
        this.duration = video.getDuration();
        this.width = video.getWidth();
        this.height = video.getHeight();
        this.size = video.getSize();
        this.timestamp = video.getTimestamp();
    }

    public YoutubeVideo toModel() {
        return new YoutubeVideo()
                .setId(id)
                .setTitle(title)
                .setDescription(description)
                .setSourceUrl(sourceUrl)
                .setAuthor(author)
                .setExt(ext)
                .setCategories(categories)
                .setTags(tags)
                .setDuration(duration)
                .setWidth(width)
                .setHeight(height)
                .setSize(size)
                .setTimestamp(timestamp);
    }
}
