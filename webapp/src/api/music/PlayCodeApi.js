import { Config, Request, SecurityContext } from './../Apis'

const PlayCodeApi = {

  get: function(successCallback, errorCallback) {
    return Request.post(Config.musicHost + "/play-code")
  }
}

export default PlayCodeApi
