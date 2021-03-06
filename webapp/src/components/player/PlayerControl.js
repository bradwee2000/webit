import { useState, useEffect } from 'react'
import { Duration, PlayPauseButton, ProgressBar, } from './../common/Commons'
import { AudioPlayer, LoopButton, PrevButton, NextButton, ShuffleButton } from './Player'

const PlayerControl = ({userState, isPlaying, eventHandler}) => {

  const [progress, setProgress] = useState(0)
  const selectedTrack = userState.selectedTrack

  const updateProgress = () => {
    if (isPlaying) {
      const progress = AudioPlayer.getProgress()
      setProgress(progress)
      if (progress === 1) {
        eventHandler.onTrackPlayFinished()
      }
    }
  }

  useEffect(() => {
    let interval;

    if (isPlaying) {
      interval = setInterval(updateProgress, 1000);
    }

    return () => {
      if (interval) {
        clearInterval(interval);
      }
    }
  }, [selectedTrack, isPlaying]);

  const onProgressChange = (progress) => {
    if (selectedTrack) {
      const adjustedCurrentTime = AudioPlayer.getDuration() * progress ;
      AudioPlayer.setCurrentTime(adjustedCurrentTime);
      setProgress(progress);
    }
  };

  const onPlay = (e) => {
    if (selectedTrack) {
      eventHandler.onTrackPlay(selectedTrack.id);
    }
  };

  const onPause = (e) => {
    if (selectedTrack) {
      eventHandler.onTrackPause(selectedTrack.id);
    }
  };

  return (
    <>
    <div className="d-flex justify-content-center control">
      <div><ShuffleButton userState={userState} onClick={eventHandler.onShuffleClick}/></div>
      <div><PrevButton onClick={eventHandler.onPrevClick}/></div>
      <div><PlayPauseButton onPlay={onPlay} onPause={onPause} isPlaying={isPlaying}/></div>
      <div><NextButton onClick={eventHandler.onNextClick}/></div>
      <div><LoopButton userState={userState} onClick={eventHandler.onLoopClick}/></div>
    </div>

    <div className="row">
        <div className="col-1">
          <small className="text-muted">
            <Duration millis={AudioPlayer.getCurrentTime() * 1000}/>
          </small>
        </div>
        <div className="col-10 d-flex flex-wrap align-content-center">
          <ProgressBar onProgressChange={onProgressChange} progress={progress}/>
        </div>
        <div className="col-1">
          <small className="text-muted">
            <Duration millis={AudioPlayer.getDuration() * 1000}/>
          </small>
        </div>
    </div>
    </>
  );
}

export default PlayerControl
