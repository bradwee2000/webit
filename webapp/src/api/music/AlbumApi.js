import { Config, Request } from './../Apis'

const AlbumApi = {

  get: function(albumId) {
    return Request.get(Config.musicHost + "/albums/" + albumId);
  },

  play: function(albumId) {
    return Request.post(Config.musicHost + "/albums/" + albumId + "/play");
  },
}

export default AlbumApi
