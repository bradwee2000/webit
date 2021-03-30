import { Config, Request } from './../Apis'

const HeosApi = {

  connect: function(pid) {
    return Request.post(Config.heosHost + "/connect")
  },

  close: function(pid) {
    return Request.post(Config.heosHost + "/close")
  },

  getPlayers: function() {
     return Request.get(Config.heosHost + "/players")
  },

  play: function(pid) {
    return Request.post(Config.heosHost + "/players/" + pid + "/play");
  },

  pause: function(pid) {
    return Request.post(Config.heosHost + "/players/" + pid + "/pause");
  },

  getVolume: function(pid) {
    return Request.get(Config.heosHost + "/players/" + pid + "/volume");
  },

  setVolume: function(pid, volume) {
    const deviceVolume = Math.floor(45 * volume)
    return Request.post(Config.heosHost + "/players/" + pid + "/volume?volume=" + deviceVolume);
  },

  getState: function(pid) {
    return Request.post(Config.heosHost + "/players/" + pid + "/state");
  }
}

export default HeosApi
