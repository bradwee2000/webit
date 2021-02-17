package com.bwee.webit.search.document;

import com.bwee.webit.model.YoutubeVideo;
import com.bwee.webit.search.model.SearchDocument;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Collections;
import java.util.List;

@Data
@Accessors(chain = true)
@Document(indexName = "youtube")
@NoArgsConstructor
public class YoutubeDocument implements SearchDocument<YoutubeVideo> {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String author;

    @Field(type = FieldType.Keyword)
    private List<String> categories = Collections.emptyList();

    @Field(type = FieldType.Keyword)
    private List<String> tags = Collections.emptyList();

    @Field(type = FieldType.Long)
    private Long duration;

    public YoutubeDocument(final YoutubeVideo video) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.description = video.getDescription();
        this.author = video.getAuthor();
        this.categories = video.getCategories();
        this.tags = video.getTags();
        this.duration = video.getDuration();
    }

    @Override
    public YoutubeVideo toModel() {
        return new YoutubeVideo().setId(id)
                .setTitle(title)
                .setDescription(description)
                .setAuthor(author)
                .setCategories(categories)
                .setTags(tags)
                .setDuration(duration);
    }
}
