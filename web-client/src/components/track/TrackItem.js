import Duration from './../common/Duration'
import PlayButton from './../common/PlayButton'

const TrackItem = ({item, playingTrack, isPlaying, compact=false, eventHandler}) => {

  const handleClick = (id) => {
    eventHandler.onTrackClick(id);
  }

  const handlePlay = (e, id) => {
    e.stopPropagation();
    eventHandler.onTrackPlay(id);
  }

  const handlePause = (e, id) => {
    e.stopPropagation();
    eventHandler.onTrackPause(id);
  }

  const isSelected = playingTrack && item.id === playingTrack.id;

  return (
    <div className={`track row rounded-3 ` + (isSelected ? "selected" : "")} onClick={() => handleClick(item.id)} role="button">
      <div className="col d-flex">
        <div className="m-2 position-relative">
          <img src={item.imageUrl} className="thumbnail-sm" alt=""/>
          <div className="position-absolute top-50 start-50 translate-middle">
            <PlayButton onPlay={(e) => handlePlay(e, item.id)} onPause={(e) => handlePause(e, item.id)} isPlaying={isSelected && isPlaying}/>
          </div>
        </div>
        <div className="m-2">
          <h6 className="my-0">{item.title}</h6>
          <small className="text-muted float-start">{item.artist}</small>
        </div>
      </div>
      { !compact &&
      <div className="col">
        <small className="text-muted">{item.albumName}</small>
      </div>
      }
      <div className="col-1 m-2 text-end mt-3">
        <Duration millis={item.durationMillis}/>
      </div>
    </div>
  )
}

export default TrackItem
