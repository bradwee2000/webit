import { useState, useEffect } from 'react'
import { Duration, PlayPauseButton, ProgressBar, } from './../common/Commons'
import { LoopButton, PrevButton, NextButton, ShuffleButton } from './Player'

const PlayerControl = ({userState, deviceService, selectedDevice, isPlaying, eventHandler}) => {

  const [progress, setProgress] = useState(0)
  const [currentTime, setCurrentTime] = useState(0)

  const selectedTrack = userState.selectedTrack
  const durationMillis = userState.selectedTrack ? userState.selectedTrack.durationMillis : 0

  const updateProgress = () => {
    if (isPlaying) {
      deviceService.getProgress(selectedDevice.id)
        .then(progress => {
          setProgress(progress)
          if (progress === 1) {
            eventHandler.onTrackPlayFinished()
          }
        })
        .catch(e => console.error(e))

      deviceService.getCurrentTime(selectedDevice.id)
        .then(setCurrentTime)
        .catch(e => console.error(e))
    }
  }

  // Update play progress
  useEffect(() => {
    let interval;

    if (isPlaying) {
      interval = setInterval(updateProgress, 1000)
    }

    return () => {
      if (interval) {
        clearInterval(interval)
      }
    }
  }, [selectedTrack, isPlaying])

  const onProgressChange = (progress) => {
    if (selectedTrack && durationMillis) {
      const adjustedCurrentTime = durationMillis * progress
      deviceService.setCurrentTime(selectedDevice.id, adjustedCurrentTime)
      setProgress(progress)
    }
  };

  const onPlay = (e) => {
    if (selectedTrack) {
      eventHandler.onTrackPlay(selectedTrack.id)
    }
  }

  const onPause = (e) => {
    if (selectedTrack) {
      eventHandler.onTrackPause(selectedTrack.id)
    }
  }

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
            <Duration millis={currentTime * 1000}/>
          </small>
        </div>
        <div className="col-10">
          <ProgressBar onProgressChange={onProgressChange} progress={progress}/>
        </div>
        <div className="col-1">
          <small className="text-muted">
            <Duration millis={durationMillis}/>
          </small>
        </div>
    </div>
    </>
  );
}

export default PlayerControl
