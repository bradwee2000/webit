import { Config, Request, SecurityContext } from './../Apis'

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
}

export default MusicUserApi
