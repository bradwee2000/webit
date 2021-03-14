package com.bwee.webit.datasource;

import com.bwee.webit.datasource.entity.AlbumEntity;
import com.bwee.webit.model.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import static java.util.Comparator.comparing;

@Service
public class AlbumDbService extends AbstractDbService<Album, AlbumEntity> {

    private final CassandraOperations cassandra;

    @Autowired
    public AlbumDbService(final CassandraOperations cassandra) {
        super(cassandra, AlbumEntity.class);
        this.cassandra = cassandra;
    }

    @Override
    public AlbumEntity merge(AlbumEntity existingEntity, AlbumEntity newEntity) {
        return newEntity;
//        final List<String> mergedTags = Stream.concat(existingEntity.getTags().stream(), newEntity.getTags().stream())
//                .distinct()
//                .collect(Collectors.toList());
//
//        final List<String> mergedArtists = Stream.concat(existingEntity.getTags().stream(), newEntity.getTags().stream())
//                .distinct().
//                collect(Collectors.toList());
//
//        final Set<String> existingTrackIds = existingEntity.getTracks().stream()
//                .map(track -> track.getTrackId())
//                .collect(Collectors.toSet());
//
//        final List<AlbumEntity.Track> newTracks = newEntity.getTracks().stream()
//                .filter(track -> !existingTrackIds.contains(track.getTrackId()))
//                .sorted(comparing(AlbumEntity.Track::))
//                .collect(Collectors.toList());
//
//        final List<AlbumEntity.Track> mergedTracks = existingEntity.getTracks().stream()
//                .sorted(comparing(AlbumEntity.Track::getTrackNum))
//                .collect(Collectors.toList());
//        mergedTracks.addAll(newTracks);
//
//        // Fix track numbers
//        int trackCounter = 1;
//        for (final AlbumEntity.Track track : mergedTracks) {
//            track.setTrackNum(trackCounter);
//            trackCounter++;
//        }
//
//        return AlbumEntity.copyOf(newEntity).setTags(mergedTags).setArtists(mergedArtists).setTracks(mergedTracks);
    }

    @Override
    public Album toModel(final AlbumEntity entity) {
        return entity.toModel();
    }

    @Override
    public AlbumEntity toEntity(final Album model) {
        return new AlbumEntity(model);
    }

}
