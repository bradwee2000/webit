import { PlayButton, Duration } from './../common/Commons'
import { Link } from 'react-router-dom';

const TrackItem = ({item, userState, isPlaying, showAlbum=true, eventHandler}) => {

  const onClick = (e, id) => {
    e.stopPropagation();
    eventHandler.onTrackClick(id);
  }

  const onPlay = (e, id) => {
    e.stopPropagation();
    eventHandler.onTrackPlay(id);
  }

  const onPause = (e, id) => {
    e.stopPropagation();
    eventHandler.onTrackPause(id);
  }

console.log(item)

  const selectedTrack = userState ? userState.selectedTrack : null
  const isSelected = selectedTrack && item.id === selectedTrack.id;
  const albumLink = "/album/" + item.albumId

  return (
    <div className={`track row rounded-3 ` + (isSelected ? "selected" : "")} onClick={(e) => onClick(e, item.id)} role="button">
      <div className="col d-flex">
        <div className="m-2 position-relative">
          <img src={item.imageUrl} className="thumbnail-sm" alt=""/>
          <div className="position-absolute top-50 start-50 translate-middle">
            <PlayButton onPlay={(e) => onPlay(e, item.id)} onPause={(e) => onPause(e, item.id)} isPlaying={isSelected && isPlaying}/>
          </div>
        </div>
        <div className="m-2">
          <h6 className="my-0">{item.title}</h6>
          <small className="text-muted float-start">{item.artist}</small>
        </div>
      </div>
      { showAlbum &&
      <div className="col">
        <small className="text-muted">
          <Link to={albumLink} className="link">{item.albumName}</Link>
        </small>
      </div>
      }
      <div className="col-1 m-2 text-end mt-3">
        <Duration millis={item.durationMillis}/>
      </div>
    </div>
  )
}

export default TrackItem
