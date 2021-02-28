class TrackPlayList {
  static remainingTracks = [];
  static finishedTracks = [];
  static isShuffle = false;
  static isLoop = false;

  static setTracks(tracks) {
    if (!Array.isArray(tracks)) {
      tracks = [tracks]; // single list
    }
    TrackPlayList.remainingTracks = tracks;
    TrackPlayList.finishedTracks = [];
    TrackPlayList._doSort();
  }

  static current() {
    return TrackPlayList.remainingTracks.length > 0 ? TrackPlayList.remainingTracks[0] : null;
  }

  static next() {
    const finishedTrack = TrackPlayList.remainingTracks.shift();
    if (finishedTrack) {
        TrackPlayList.finishedTracks.push(finishedTrack);
    }
    TrackPlayList._handleLoop();
    return TrackPlayList.remainingTracks.length > 0 ? TrackPlayList.remainingTracks[0] : null;
  }

  static prev() {
    const prevTrack = TrackPlayList.finishedTracks.pop();
    if (prevTrack) {
        TrackPlayList.remainingTracks.unshift(prevTrack);
    }
    return prevTrack;
  }

  static setShuffle(isShuffle) {
    TrackPlayList.isShuffle = isShuffle;
    TrackPlayList._doSort();
  }

  static setLoop(isLoop) {
    TrackPlayList.isLoop = isLoop;
    TrackPlayList._doSort();
  }

  static _doSort() {
    if (TrackPlayList.isShuffle) {
      TrackPlayList._doShuffle();
    } else {
      TrackPlayList.remainingTracks.sort(function (a, b) { return a.track - b.track })
    }
  }

  static _doShuffle() {
    const tracks = TrackPlayList.remainingTracks;
    const currentTrack = tracks.shift();
    for(let i = tracks.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * i)
      const temp = tracks[i]
      tracks[i] = tracks[j]
      tracks[j] = temp
    }
    tracks.unshift(currentTrack);
  }

  static _handleLoop() {
    if (TrackPlayList.isLoop && TrackPlayList.remainingTracks.length === 0) {
      TrackPlayList.remainingTracks = TrackPlayList.finishedTracks;
      TrackPlayList.finishedTracks = [];
      TrackPlayList._doSort();
    }
  }
}

export default TrackPlayList
