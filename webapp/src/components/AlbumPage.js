import { useParams } from "react-router-dom";
import { useState, useEffect } from 'react'
import TrackSection from './track/TrackSection';
import { AlbumApi } from './../api/Apis';
import { ArtistList, PlayPauseButton } from './common/Commons'

function AlbumPage({isPlaying, userState, eventHandler}) {
  const { albumId } = useParams()
  const [album, setAlbum] = useState()

  const selectedAlbum = userState.selectedAlbum
  const isThisAlbumSelected = selectedAlbum && albumId === selectedAlbum.id
  const isThisAlbumPlaying = isPlaying && isThisAlbumSelected
  const numOfSongs = (album && album.tracks) ? album.tracks.length : 0

  const onAlbumPlay = (e) => {
    eventHandler.onAlbumPlay(albumId)
  }

  const onAlbumPause = (e) => {
    eventHandler.onAlbumPause(albumId)
  }

  const onTrackPlay = (trackId) => {
    eventHandler.onAlbumPlay(albumId, trackId)
  }

  const onTrackPause = (trackId) => {
    eventHandler.onAlbumPause(albumId, trackId)
  }

  const onTrackClick = (trackId) => {
    eventHandler.onAlbumPlay(albumId, trackId)
  }

  const onArtistClick = (artist) => {
    eventHandler.onArtistClick(artist)
  }

  // Fetch album details
  useEffect(() => {
    AlbumApi.get(albumId)
      .then(setAlbum)
      .catch(e => {
        console.error(e)
      })
  }, [albumId])

  const localEventhandler = {
    onTrackPlay,
    onTrackPause,
    onTrackClick,
    onArtistClick
  }

  if (!album) {
    return <div>Album not found.</div>
  }

  return (
    <div className="album-page">
      <div className="mb-4">
        <div className="row">
          <div className="col-12 col-md-auto">
            <img src={album.imageUrl} width="200" height="200" alt={album.name}/>
          </div>
          <div className="col-12 col-md-auto px-md-4 pt-2 d-flex flex-column align-items-start">
            <h1 className="mt-auto">{album.name}</h1>
            <div>
              <small>
                <ArtistList artists={album.artists} eventHandler={eventHandler}/>
              </small> &#9702;
              <small>{album.year}</small> &#9702;
              <small>{numOfSongs} songs</small>
            </div>
          </div>
        </div>
      </div>
      <PlayPauseButton onPlay={onAlbumPlay} onPause={onAlbumPause} isPlaying={isThisAlbumPlaying} className="large primary mb-3"/>
      <TrackSection tracks={album.tracks} userState={userState} isPlaying={isPlaying} showAlbum={false} showTrackNum={true} eventHandler={localEventhandler} className="mb-5" title=""/>
    </div>
  )
}

export default AlbumPage;
