class AudioPlayer {
  static audio = new Audio()
  static isPlaying = false

  static play(trackUrl) {

    if (AudioPlayer.audio.src !== trackUrl) {
      AudioPlayer.audio.pause()
      AudioPlayer.audio.src = trackUrl
    }

    return AudioPlayer.audio.play()
      .then(() => AudioPlayer.isPlaying = true)
      .catch(e => AudioPlayer.isPlaying = false)
  }

  static pause() {
    if (AudioPlayer.isPlaying) {
      AudioPlayer.audio.pause()
      AudioPlayer.isPlaying = false
    }
  }

  static isPlaying() {
    return AudioPlayer.isPlaying
  }

  static getCurrentTime() {
    return AudioPlayer.audio.currentTime
  }

  static setCurrentTime(currentTime) {
    if (!isNaN(currentTime)) {
      AudioPlayer.audio.currentTime = currentTime
    }
  }

  static getDuration() {
    return AudioPlayer.audio.duration;
  }

  static getProgress() {
    return AudioPlayer.getCurrentTime() / AudioPlayer.getDuration()
  }

  static getVolume() {
    return AudioPlayer.audio.volume;
  }

  static setVolume(volume) {
    AudioPlayer.audio.volume = volume;
  }
}

export default AudioPlayer
