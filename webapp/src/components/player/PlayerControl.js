import { useState, useEffect } from 'react'
import LoopButton from './LoopButton'
import PlayPauseButton from './../common/PlayPauseButton'
import PrevButton from './PrevButton'
import NextButton from './NextButton'
import ShuffleButton from './ShuffleButton'
import ProgressBar from './../common/ProgressBar'
import TrackApi from './../../api/TrackApi'
import AudioPlayer from './AudioPlayer'
import Duration from './../common/Duration'
import TrackInfo from './TrackInfo'

const PlayerControl = ({playingTrack, isPlaying, eventHandler}) => {

  const [progress, setProgress] = useState(0);

  useEffect(() => {
    if (playingTrack) {
      if (isPlaying) {
        AudioPlayer.play(TrackApi.play(playingTrack.id));
      } else {
        AudioPlayer.pause();
      }
    }

    const interval = setInterval(() => {
      if (playingTrack && isPlaying) {
        const progress = AudioPlayer.getCurrentTime() / AudioPlayer.getDuration();
        setProgress(progress);
      }
    }, 1000);
    return () => clearInterval(interval);
  }, [playingTrack, isPlaying]);

  const handleProgressChange = (progress) => {
    const adjustedCurrentTime = AudioPlayer.getDuration() * progress ;
    AudioPlayer.setCurrentTime(adjustedCurrentTime);
    setProgress(progress);
  };

  const handleOnPlay = (e) => {
    if (playingTrack) {
      eventHandler.onTrackPlay(playingTrack.id);
    }
  };

  const handleOnPause = (e) => {
    if (playingTrack) {
      eventHandler.onTrackPause(playingTrack.id);
    }
  };

  return (
    <>
    <div className="d-flex justify-content-center control">
      <div><ShuffleButton onClick={eventHandler.onShuffleClick}/></div>
      <div><PrevButton onClick={eventHandler.onPrevClick}/></div>
      <div><PlayPauseButton onPlay={handleOnPlay} onPause={handleOnPause} isPlaying={isPlaying}/></div>
      <div><NextButton onClick={eventHandler.onNextClick}/></div>
      <div><LoopButton onClick={eventHandler.onLoopClick}/></div>
    </div>

    <div className="row">
        <div className="col-1">
          <small className="text-muted">
            <Duration millis={AudioPlayer.getCurrentTime() * 1000}/>
          </small>
        </div>
        <div className="col-10 d-flex flex-wrap align-content-center">
          <ProgressBar onProgressChange={handleProgressChange} progress={progress}/>
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
