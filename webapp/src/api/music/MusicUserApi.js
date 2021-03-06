import { Config, Request } from './../Apis'

const MusicUserApi = {

  get: function() {
    return Request.get(Config.musicHost + "/user");
  },

  shuffle: function(isShuffle) {
    return Request.post(Config.musicHost + "/user/shuffle?isShuffle=" + isShuffle);
  },

  loop: function(isLoop) {
    return Request.post(Config.musicHost + "/user/loop?isLoop=" + isLoop);
  },

  next: function() {
    return Request.post(Config.musicHost + "/user/next");
  },

  prev: function() {
    return Request.post(Config.musicHost + "/user/prev");
  },

  selectTrack: function(trackId) {
    return Request.post(Config.musicHost + "/user/tracks/" + trackId + "/play")
  }
}

export default MusicUserApi
