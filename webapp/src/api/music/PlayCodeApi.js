import { Config, Request, SecurityContext } from './../Apis'

const PlayCodeApi = {

  get: function(successCallback, errorCallback) {
    return Request.get(Config.musicHost + "/play-code")
  }
}

export default PlayCodeApi
