import { useParams } from "react-router-dom";
import { useState, useEffect } from 'react'
import TrackSection from './track/TrackSection';
import { AlbumApi } from './../api/Apis';

function AlbumPage({isPlaying, userState, eventHandler}) {

  const { albumId } = useParams()
  const [album, setAlbum] = useState([])

  useEffect(() => {
    AlbumApi.get(albumId).then(res => {console.log(res);setAlbum(res)})
  }, [albumId])

  const numOfSongs = (album && album.tracks) ? album.tracks.length : 0

  return (
    <>
      <div className="d-flex flex-row mb-5 align-items-end">
        <div><img src={album.imageUrl} width="200" height="200"/></div>
        <div className="px-4">
          <h1>{album.name}</h1>
          <small>{album.artists}</small> &#9702; <small>{album.year}</small> &#9702; <small>{numOfSongs} songs</small>
        </div>
      </div>
      <TrackSection tracks={album.tracks} userState={userState} isPlaying={isPlaying} showAlbum={false} eventHandler={eventHandler} className="mb-4" title=""/>
    </>
  );
}

export default AlbumPage;
