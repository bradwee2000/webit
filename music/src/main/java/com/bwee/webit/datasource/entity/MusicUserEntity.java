package com.bwee.webit.datasource.entity;

import com.bwee.webit.model.MusicUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.LIST;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.TEXT;

@Table("musicUser")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class MusicUserEntity implements Entity<MusicUserEntity, String> {

    public static MusicUserEntity copyOf(final MusicUserEntity e) {
        final MusicUserEntity copy = new MusicUserEntity();
        BeanUtils.copyProperties(e, copy);
        return copy;
    }

    @PrimaryKey
    private String id;

    @CassandraType(type = LIST, typeArguments = TEXT)
    private List<String> trackIdQueue = emptyList();

    private Integer currentTrackIndex;

    private Boolean isShuffle;

    private Boolean isLoop;

    private Boolean isPlaying;

    private String selectedDeviceId;

    public MusicUserEntity(final MusicUser user) {
        this.id = user.getId();
        this.trackIdQueue = user.getTrackIdQueue();
        this.currentTrackIndex = user.getCurrentTrackIndex();
        this.isShuffle = user.isShuffle();
        this.isLoop = user.isLoop();
        this.isPlaying = user.isPlaying();
    }

    public MusicUser toModel() {
        return new MusicUser()
                .setId(id)
                .setTrackIdQueue(trackIdQueue == null ? Collections.emptyList() : trackIdQueue)
                .setCurrentTrackIndex(currentTrackIndex)
                .setShuffle(isShuffle == null ? false : isShuffle)
                .setLoop(isLoop == null ? false : isLoop)
                .setPlaying(isPlaying == null ? false : isPlaying);
    }
}
