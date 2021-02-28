class AudioPlayer {
  static audio = new Audio();

  static play(trackUrl) {
    if (AudioPlayer.audio.src !== trackUrl) {
        AudioPlayer.audio.pause();
        AudioPlayer.audio.src = trackUrl;
    }
    AudioPlayer.audio.play()
  }

  static pause() {
    AudioPlayer.audio.pause();
  }

  static getCurrentTime() {
    return AudioPlayer.audio.currentTime;
  }

  static setCurrentTime(currentTime) {
    AudioPlayer.audio.currentTime = currentTime;
  }

  static getDuration() {
    return AudioPlayer.audio.duration;
  }
}

export default AudioPlayer
