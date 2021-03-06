import { useState, useEffect } from 'react'
import { useHistory } from 'react-router-dom';
import { ProgressBar } from './../common/Commons'
import { AudioPlayer, DeviceButton, MuteButton, QueueButton } from './Player'

const PlayerOtherControls = ({userState, eventHandler}) => {

  const history = useHistory()
  const [volume, setVolume] = useState(1)
  let lastVolume = 0.1 //default volume if unmute

  const onVolumeChange = (newVolume) => {
    const snappedVolume = Math.round(newVolume * 10) / 10 // Snap to nearest 10s
    AudioPlayer.setVolume(snappedVolume)
    if (volume > 0) {
      lastVolume = volume
    }
    setVolume(snappedVolume)
  }

  const onMuteClick = (e) => {
    e.stopPropagation()
    if (volume === 0) {
      setVolume(lastVolume)
    }
  }

  const toggleQueue = (isEnabled, e) => {
    e.stopPropagation()
    if (isEnabled) {
      history.push({pathname: '/queue'})

    } else {
      history.goBack()
    }
  }

  const playerVolume = AudioPlayer.getVolume()

  useEffect(() => {
      setVolume(playerVolume)
  }, [playerVolume])

  return (
    <>
      <div className="col-6"></div>
      <div className="col"><QueueButton onClick={toggleQueue}/></div>
      <div className="col"><DeviceButton/></div>
      <div className="col"><MuteButton volume={volume} onClick={onMuteClick}/></div>
      <div className="col-3 d-flex align-items-center">
        <ProgressBar onProgressChange={onVolumeChange} progress={volume}/>
      </div>
    </>
  );
}

export default PlayerOtherControls
