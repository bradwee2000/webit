import { useState } from 'react'

const PlayPauseButton = ({onPlay, onPause, isPlaying=false}) => {

  const handleClick = (e) => {
    if (isPlaying) {
      onPause(e);
    } else {
      onPlay(e);
    }
  }

  return (
    <>
      <button className={'btn ' + (isPlaying ? 'pause' : 'play')} onClick={handleClick}><div className="icon"></div></button>
    </>
  )
}

export default PlayPauseButton
