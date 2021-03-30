import { HeosApi } from './../../api/Apis'

const HeosDevice = {

  connect: () => {
    return HeosApi.connect()
  },

  close: () => {
    return HeosApi.close()
  },

  play: (deviceId) => {
    return HeosApi.play(deviceId)
  },

  pause: (deviceId) => {
    return HeosApi.pause(deviceId)
  },

  isPlaying: () =>  {
    return false
  },

  getCurrentTime: (deviceId) => {
    return HeosApi.getState(deviceId).then(state => state.currentProgress)
  },

  setCurrentTime: (deviceId, currentTime) => {

  },

  getDuration: (deviceId) => {
    return HeosApi.getState(deviceId)
      .then(res => res.duration)
  },

  getProgress: (deviceId) => {
    return HeosApi.getState(deviceId).then(state => (state.duration && state.duration) > 0 ? state.currentProgress / state.duration : 0)
  },

  getVolume: (deviceId) => {
    return HeosApi.getVolume(deviceId)
  },

  setVolume: (deviceId, volume) => {
    return HeosApi.setVolume(deviceId, volume)
  },

  getType: () => {
    return "heos"
  }
}

export default HeosDevice
