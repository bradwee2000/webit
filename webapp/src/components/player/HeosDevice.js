import { HeosApi } from './../../api/Apis'

const HeosDevice = {
  play: (deviceId) => {
    return HeosApi.play(deviceId)
  },

  pause: (deviceId) => {
    return HeosApi.pause(deviceId)
  },

  isPlaying: () =>  {
    return false
  },

  getCurrentTime: () => {
    return 0
  },

  setCurrentTime: (deviceId, currentTime) => {

  },

  getDuration: () => {
    return 0
  },

  getProgress: () => {
    return 0
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
