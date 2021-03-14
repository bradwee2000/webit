package com.bwee.webit.service;

import com.bwee.webit.auth.AuthenticationService;
import com.bwee.webit.datasource.AlbumDbService;
import com.bwee.webit.datasource.MusicUserDbService;
import com.bwee.webit.exception.AlbumNotFoundException;
import com.bwee.webit.model.MusicUser;
import com.bwee.webit.model.Track;
import com.bwee.webit.datasource.TrackDbService;
import com.bwee.webit.exception.MusicUserNotFoundException;
import com.bwee.webit.exception.TrackNotFoundException;
import com.bwee.webit.model.Album;
import com.bwee.webit.service.function.GetTrackQueueStrategy;
import com.bwee.webit.service.function.ShuffleSortStrategy;
import com.bwee.webit.service.function.TrackNumSortStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Slf4j
@Service
public class MusicUserService extends SimpleCrudService<MusicUser> {

    private final AuthenticationService authenticationService;
    private final MusicUserDbService userDbService;
    private final AlbumDbService albumDbService;
    private final TrackDbService trackDbService;
    private final TrackNumSortStrategy trackNumSortStrategy;
    private final ShuffleSortStrategy shuffleSortStrategy;
    private final GetTrackQueueStrategy getTrackQueueStrategy;

    @Autowired
    public MusicUserService(final AlbumDbService albumDbService,
                            final AuthenticationService authenticationService,
                            final MusicUserDbService userDbService,
                            final TrackDbService trackDbService,
                            final TrackNumSortStrategy trackNumSortStrategy,
                            final ShuffleSortStrategy shuffleSortStrategy,
                            final GetTrackQueueStrategy getTrackQueueStrategy) {
        super(userDbService);
        this.albumDbService = albumDbService;
        this.authenticationService = authenticationService;
        this.userDbService = userDbService;
        this.trackDbService = trackDbService;
        this.trackNumSortStrategy = trackNumSortStrategy;
        this.shuffleSortStrategy = shuffleSortStrategy;
        this.getTrackQueueStrategy = getTrackQueueStrategy;
    }

    @Override
    public MusicUser findByIdStrict(final String id) {
        return findById(id).orElseThrow(() -> new MusicUserNotFoundException(id));
    }

    public MusicUser getLoginUser() {
        return userDbService.findByIdOrCreate(authenticationService.getLoginUserId());
    }

    public List<Track> getQueue() {
        final MusicUser user = getLoginUser();
        final List<String> trackIds = getTrackQueueStrategy.apply(user);
        return trackDbService.findByIdsSorted(trackIds);
    }

    public MusicUser nextTrack() {
        final MusicUser user = getLoginUser();

        if (user.getTrackIdQueue().isEmpty()) {
            return user;
        }

        int nextTrackNum = user.isLoop() ?
                (user.getCurrentTrackIndex() + 1) % user.getTrackIdQueue().size() :
                Math.min(user.getCurrentTrackIndex() + 1, user.getTrackIdQueue().size() - 1);

        // Update if there's a difference
        if (user.getCurrentTrackIndex() != nextTrackNum) {
            userDbService.updateCurrentTrackIndex(user.setCurrentTrackIndex(nextTrackNum));
        }

        return user;
    }

    public MusicUser prevTrack() {
        final MusicUser user = getLoginUser();

        if (user.getTrackIdQueue().isEmpty()) {
            return user;
        }

        final int prevTrackNum = Math.max(0,
                Math.min(user.getCurrentTrackIndex() - 1, user.getTrackIdQueue().size() - 1));

        // Update if there's a difference
        if (user.getCurrentTrackIndex() != prevTrackNum) {
            userDbService.updateCurrentTrackIndex(user.setCurrentTrackIndex(prevTrackNum));
        }

        return user;
    }

    public MusicUser updateShuffle(final boolean isShuffle) {
        final MusicUser user = getLoginUser();

        if (user.isShuffle() == isShuffle) {
            return user;
        }

        final MusicUser updatedUser = isShuffle ?
                shuffleSortStrategy.apply(user) :
                trackNumSortStrategy.apply(user);

        updatedUser.setShuffle(isShuffle);

        userDbService.update(MusicUserDbService.Update.of(user.getId())
                .isShuffle(updatedUser.isShuffle())
                .trackIdQueue(updatedUser.getTrackIdQueue())
        );

        return updatedUser;
    }

    public MusicUser updateLoop(final boolean isLoop) {
        final MusicUser user = getLoginUser();
        userDbService.update(MusicUserDbService.Update.of(user.getId()).isLoop(user.isLoop()));
        return user;
    }

    public MusicUser playTrackFromQueue(final String trackId) {
        return playTrack(trackId, false);
    }

    public MusicUser playTrack(final String trackId) {
        return playTrack(trackId, true);
    }

    private MusicUser playTrack(final String trackId, final boolean isOverwriteQueue) {
        final MusicUser user = getLoginUser();
        final Track track = trackDbService.findById(trackId)
                .orElseThrow(() -> new TrackNotFoundException(trackId));

        if (isOverwriteQueue) {
            user.setCurrentTrackIndex(0).setTrackIdQueue(singletonList(trackId));
        } else {
            final int trackIndex = user.getTrackIdQueue().indexOf(trackId);
            user.setCurrentTrackIndex(trackIndex);
        }

        userDbService.update(MusicUserDbService.Update.of(user.getId())
                .isPlaying(user.isPlaying())
                .currentTrackIndex(user.getCurrentTrackIndex())
                .trackIdQueue(user.getTrackIdQueue()));
        return user;
    }

    public MusicUser playAlbum(final String albumId) {
        return playAlbum(albumDbService.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(albumId)));
    }

    public MusicUser playAlbum(final Album album) {

        final List<String> trackIds = trackDbService.findByAlbumId(album.getId()).stream()
                .map(t -> t.getId())
                .collect(Collectors.toList());

        final MusicUser user = getLoginUser()
                .setCurrentTrackIndex(0)
                .setTrackIdQueue(trackIds)
                .setPlaying(true);

        userDbService.update(MusicUserDbService.Update.of(user.getId())
                .isPlaying(user.isPlaying())
                .currentTrackIndex(user.getCurrentTrackIndex())
                .trackIdQueue(user.getTrackIdQueue()));

        return user;
    }
}
