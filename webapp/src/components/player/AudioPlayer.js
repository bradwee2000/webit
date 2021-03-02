class AudioPlayer {
  static audio = new Audio();
  static isPlaying = false;

  static play(trackUrl) {
    if (AudioPlayer.audio.src !== trackUrl) {
      AudioPlayer.audio.pause();
      AudioPlayer.audio.src = trackUrl;
    }

    AudioPlayer.audio.play()
      .then(() => AudioPlayer.isPlaying = true)
      .catch(e => AudioPlayer.isPlaying = false);
  }

  static pause() {
    if (AudioPlayer.isPlaying) {
      AudioPlayer.audio.pause();
      AudioPlayer.isPlaying = false;
    }
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
