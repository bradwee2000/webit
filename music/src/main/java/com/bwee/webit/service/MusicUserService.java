package com.bwee.webit.service;

import com.bwee.webit.auth.AuthenticationService;
import com.bwee.webit.core.RandomIdGenerator;
import com.bwee.webit.core.SimpleCrudService;
import com.bwee.webit.datasource.MusicUserDbService;
import com.bwee.webit.datasource.TrackDbService;
import com.bwee.webit.exception.MusicUserNotFoundException;
import com.bwee.webit.model.MusicUser;
import com.bwee.webit.model.Track;
import com.bwee.webit.service.function.GetTrackQueueStrategy;
import com.bwee.webit.service.function.ShuffleSortStrategy;
import com.bwee.webit.service.function.TrackNumSortStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MusicUserService extends SimpleCrudService<MusicUser> {

    private final AuthenticationService authenticationService;
    private final MusicUserDbService userDbService;
    private final TrackDbService trackDbService;
    private final TrackNumSortStrategy trackNumSortStrategy;
    private final ShuffleSortStrategy shuffleSortStrategy;
    private final GetTrackQueueStrategy getTrackQueueStrategy;
    private final RandomIdGenerator idGenerator;

    @Autowired
    public MusicUserService(final AuthenticationService authenticationService,
                            final MusicUserDbService userDbService,
                            final TrackDbService trackDbService,
                            final TrackNumSortStrategy trackNumSortStrategy,
                            final ShuffleSortStrategy shuffleSortStrategy,
                            final GetTrackQueueStrategy getTrackQueueStrategy,
                            final RandomIdGenerator idGenerator) {
        super(userDbService);
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

    public Optional<Track> nextTrack() {
        final MusicUser user = findByIdStrict(authenticationService.getLoginUserId());

        if (user.getTrackIdQueue().isEmpty()) {
            return Optional.empty();
        }

        int nextTrackNum = user.isLoop() ?
                (user.getCurrentTrackIndex() + 1) % user.getTrackIdQueue().size() :
                Math.min(user.getCurrentTrackIndex() + 1, user.getTrackIdQueue().size() - 1);

        // Update if there's a difference
        if (user.getCurrentTrackIndex() != nextTrackNum) {
            userDbService.updateCurrentTrackIndex(user.setCurrentTrackIndex(nextTrackNum));
        }

        return trackDbService.findById(user.getTrackIdQueue().get(nextTrackNum));
    }

    public Optional<Track> prevTrack() {
        final MusicUser user = findByIdStrict(authenticationService.getLoginUserId());

        if (user.getTrackIdQueue().isEmpty()) {
            return Optional.empty();
        }

        final int prevTrackNum = Math.max(0,
                Math.min(user.getCurrentTrackIndex() - 1, user.getTrackIdQueue().size() - 1));

        // Update if there's a difference
        if (user.getCurrentTrackIndex() != prevTrackNum) {
            userDbService.updateCurrentTrackIndex(user.setCurrentTrackIndex(prevTrackNum));
        }

        return trackDbService.findById(user.getTrackIdQueue().get(prevTrackNum));
    }

    public MusicUser updateShuffle(final boolean isShuffle) {
        final MusicUser user = findByIdStrict(authenticationService.getLoginUserId());

        if (user.isShuffle() == isShuffle) {
            return user;
        }

        final MusicUser updatedUser = isShuffle ?
                shuffleSortStrategy.apply(user) :
                trackNumSortStrategy.apply(user);

        userDbService.updateShuffle(updatedUser);

        return updatedUser;
    }

    public MusicUser updateLoop(final boolean isLoop) {
        final MusicUser user = findByIdStrict(authenticationService.getLoginUserId());
        if (user.isLoop() == isLoop) {
            return user;
        }
        user.setLoop(isLoop);
        userDbService.updateLoop(user);
        return user;
    }

    public MusicUser updateTrackQueue(final List<String> trackIds) {
        final MusicUser user = findByIdStrict(authenticationService.getLoginUserId())
                .setTrackIdQueue(trackIds)
                .setCurrentTrackIndex(0);
        userDbService.updateTrackIds(user);
        return user;
    }
}
