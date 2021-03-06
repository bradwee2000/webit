import { Config, Request } from './../Apis'

const AlbumApi = {

  get: function(albumId) {
    return Request.get(Config.musicHost + "/albums/" + albumId);
  },

  play: function(albumId, trackId = null) {
    if (trackId) {
      return Request.post(Config.musicHost + "/albums/" + albumId + "/tracks/" + trackId + "/play");
    }
    return Request.post(Config.musicHost + "/albums/" + albumId + "/play");
  },
}

export default AlbumApi
