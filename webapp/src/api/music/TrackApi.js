import { Config, Request, SecurityContext } from './../Apis'

const TrackApi = {

  get: function(trackId) {
    return Request.get(Config.musicHost + "/tracks/" + trackId);
  },

  play: function(trackId) {
    return Request.post(Config.musicHost + "/tracks/" + trackId + "/play");
  },

  getStreamUrl: function(trackId) {
    const playToken = SecurityContext.getPlayTokenHash(trackId);
    return "http://localhost:8080/music/tracks/" + trackId + "/stream?token=" + playToken;
  }
}

export default TrackApi