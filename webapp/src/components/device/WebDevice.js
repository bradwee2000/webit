import { MusicUserApi, TrackApi } from './../../api/Apis'
import { AudioPlayer } from './../player/Player'

const WebDevice = {

  connect: () => {
    // do nothing
  },

  close: () => {
    return AudioPlayer.pause()
  },

  play: (deviceId) => {
    MusicUserApi.get().then(res => {
      if (res.selectedTrack) {
        return AudioPlayer.play(TrackApi.getStreamUrl(res.selectedTrack.id))
      }
    })
  },

  pause: () => {
    return AudioPlayer.pause()
  },

  isPlaying: () =>  {
    return AudioPlayer.isPlaying()
  },

  getCurrentTime: () => {
    return AudioPlayer.getCurrentTime()
  },

  setCurrentTime: (deviceId, currentTimeMillis) => {
    AudioPlayer.setCurrentTime(currentTimeMillis / 1000)
  },

  getDuration: () => {
    return new Promise((resolve, reject) => {
      resolve(AudioPlayer.getDuration())
    })
  },

  getProgress: () => {
    return new Promise((resolve, reject) => {
      resolve(AudioPlayer.getProgress())
    })
  },

  getVolume: () => {
    return new Promise((resolve, reject) => {
      resolve(AudioPlayer.getVolume())
    })
  },

  setVolume: (deviceId, volume) => {
    return AudioPlayer.setVolume(volume)
  },

  getType: () => {
    return "web"
  }
}

export default WebDevice
