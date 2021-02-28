const PlayButton = ({onPlay, onPause, isPlaying}) => {

  const onClick = isPlaying ? onPause : onPlay;

  return (
    <>
      <button className={'btn ' + (isPlaying ? 'pause' : 'play')} onClick={onClick}><div className="icon"></div></button>
    </>
  )
}

export default PlayButton
