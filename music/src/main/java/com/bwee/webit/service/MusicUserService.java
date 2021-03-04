package com.bwee.webit.service;

import com.bwee.webit.auth.AuthenticationService;
import com.bwee.webit.core.RandomIdGenerator;
import com.bwee.webit.core.SimpleCrudService;
import com.bwee.webit.datasource.AlbumDbService;
import com.bwee.webit.datasource.MusicUserDbService;
import com.bwee.webit.datasource.TrackDbService;
import com.bwee.webit.exception.AlbumNotFoundException;
import com.bwee.webit.exception.MusicUserNotFoundException;
import com.bwee.webit.exception.TrackNotFoundException;
import com.bwee.webit.model.Album;
import com.bwee.webit.model.MusicUser;
import com.bwee.webit.model.Track;
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

    private final AlbumDbService albumDbService;
    private final AuthenticationService authenticationService;
    private final MusicUserDbService userDbService;
    private final TrackDbService trackDbService;
    private final TrackNumSortStrategy trackNumSortStrategy;
    private final ShuffleSortStrategy shuffleSortStrategy;
    private final GetTrackQueueStrategy getTrackQueueStrategy;
    private final RandomIdGenerator idGenerator;

    @Autowired
    public MusicUserService(final AlbumDbService albumDbService,
                            final AuthenticationService authenticationService,
                            final MusicUserDbService userDbService,
                            final TrackDbService trackDbService,
                            final TrackNumSortStrategy trackNumSortStrategy,
                            final ShuffleSortStrategy shuffleSortStrategy,
                            final GetTrackQueueStrategy getTrackQueueStrategy,
                            final RandomIdGenerator idGenerator) {
        super(userDbService);
        this.albumDbService = albumDbService;
        this.authenticationService = authenticationService;
        this.userDbService = userDbService;
        this.trackDbService = trackDbService;
        this.trackNumSortStrategy = trackNumSortStrategy;
        this.shuffleSortStrategy = shuffleSortStrategy;
        this.getTrackQueueStrategy = getTrackQueueStrategy;
        this.idGenerator = idGenerator;
    }

    @Override
    public MusicUser findByIdStrict(final String id) {
        return findById(id).orElseThrow(() -> new MusicUserNotFoundException(id));
    }

    public MusicUser insertNewUser(final MusicUser user) {
        user.setId(idGenerator.generateId(user));
        save(user);
        return user;
    }

    public MusicUser getLoginUser() {
        return findByIdStrict(authenticationService.getLoginUserId());
    }

    public List<Track> getQueue() {
        final MusicUser user = findByIdStrict(authenticationService.getLoginUserId());
        final List<String> trackIds = getTrackQueueStrategy.apply(user);
        return trackDbService.findByIdsSorted(trackIds);
    }

    public MusicUser nextTrack() {
        final MusicUser user = findByIdStrict(authenticationService.getLoginUserId());

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
        final MusicUser user = findByIdStrict(authenticationService.getLoginUserId());

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
        final String userId = authenticationService.getLoginUserId();
        final MusicUser user = findByIdStrict(authenticationService.getLoginUserId());

        if (user.isShuffle() == isShuffle) {
            return user;
        }

        final MusicUser updatedUser = isShuffle ?
                shuffleSortStrategy.apply(user) :
                trackNumSortStrategy.apply(user);

        updatedUser.setShuffle(isShuffle);

        userDbService.update(MusicUserDbService.Update.of(userId)
                .isShuffle(updatedUser.isShuffle())
                .trackIdQueue(updatedUser.getTrackIdQueue())
        );

        return updatedUser;
    }

    public MusicUser updateLoop(final boolean isLoop) {
        final String userId = authenticationService.getLoginUserId();
        final MusicUser user = findByIdStrict(userId).setLoop(isLoop);

        userDbService.update(MusicUserDbService.Update.of(userId).isLoop(user.isLoop()));

        return user;
    }

    public MusicUser playTrack(final String trackId) {
        final String userId = authenticationService.getLoginUserId();
        final Track track = trackDbService.findById(trackId)
                .orElseThrow(() -> new TrackNotFoundException(trackId));

        final MusicUser user = findByIdStrict(userId)
                .setCurrentTrackIndex(0)
                .setTrackIdQueue(singletonList(trackId))
                .setPlaying(true);

        userDbService.update(MusicUserDbService.Update.of(userId)
                .isPlaying(user.isPlaying())
                .currentTrackIndex(user.getCurrentTrackIndex())
                .trackIdQueue(user.getTrackIdQueue()));
        return user;
    }

    public MusicUser playAlbum(final Album album) {
        final String userId = authenticationService.getLoginUserId();
        final List<String> trackIds = album.getTracks().stream().map(t -> t.getId()).collect(Collectors.toList());

        final MusicUser user = findByIdStrict(userId)
                .setCurrentTrackIndex(0)
                .setTrackIdQueue(trackIds)
                .setPlaying(true);

        userDbService.update(MusicUserDbService.Update.of(userId)
                .isPlaying(user.isPlaying())
                .currentTrackIndex(user.getCurrentTrackIndex())
                .trackIdQueue(user.getTrackIdQueue()));

        return user;
    }
}
