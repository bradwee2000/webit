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

  setCurrentTime: (deviceId, currentTime) => {
    AudioPlayer.setCurrentTime(currentTime)
  },

  getDuration: () => {
    return AudioPlayer.getDuration()
  },

  getProgress: () => {
    return AudioPlayer.getProgress()
  },

  getVolume: () => {
    return AudioPlayer.getVolume()
  },

  setVolume: (deviceId, volume) => {
    return AudioPlayer.setVolume(volume)
  },

  getType: () => {
    return "web"
  }
}

export default WebDevice
