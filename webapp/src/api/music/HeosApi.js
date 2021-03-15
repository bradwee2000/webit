import { Config, Request } from './../Apis'

const HeosApi = {

  getPlayers: function() {
    return Request.get(Config.heosHost + "/players");
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
    return Request.post(Config.heosHost + "/players/" + pid + "/volume?volume=" + volume);
  },
}

export default HeosApi
