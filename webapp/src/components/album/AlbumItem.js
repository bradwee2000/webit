import AlbumTitle from './AlbumTitle'

import { ArtistList, PlayPauseButton } from './../common/Commons'

const AlbumItem = ({album, userState, isPlaying, eventHandler}) => {

  const selectedAlbum = userState.selectedAlbum

  const handleAlbumClick = (id) => {
    eventHandler.onAlbumClick(id);
  }

  const handleAlbumPlay = (e, id) => {
    e.stopPropagation();
    eventHandler.onAlbumPlay(id);
  }

  const handleAlbumPause = (e, id) => {
    e.stopPropagation();
    eventHandler.onAlbumPause(id);
  }

  const isSelected = selectedAlbum && album.id === selectedAlbum.id;
  const isThisAlbumPlaying = isSelected && isPlaying

  return (
    <div className="col col-6 col-md-4 col-lg-2 col-lg-3 col-xl-2 p-0">
      <div className={"album lh-sm text-light border-1 rounded-1 m-1 p-3 " + (isSelected ? "selected" : "")} role="button" onClick={() => handleAlbumClick(album.id)}>
        <div className="position-relative">
          <img src={album.imageUrl} className="img w-100" alt=""/>
          <div className="position-absolute bottom-0 end-0 m-2">
            <PlayPauseButton onPlay={(e) => handleAlbumPlay(e, album.id)} onPause={(e) => handleAlbumPause(e, album.id)} isPlaying={isThisAlbumPlaying} className="primary"/>
          </div>
        </div>
        <h6 className="my-0 mt-3 mb-1">
          <AlbumTitle title={album.name}/>
        </h6>
        <small className="text-muted">
          <ArtistList artists={album.artists} eventHandler={eventHandler}/>
        </small>
      </div>
    </div>
  )
}

export default AlbumItem
