import { useParams } from "react-router-dom";
import { useState, useEffect } from 'react'
import TrackSection from './track/TrackSection';
import { AlbumApi } from './../api/Apis';

function AlbumPage({isPlaying, userState, eventHandler}) {

  const { albumId } = useParams()
  const [album, setAlbum] = useState([])

  useEffect(() => {
    AlbumApi.get(albumId).then(res => setAlbum(res))
  }, [albumId])

  return (
    <>
      <img src={album.imageUrl} width="200" height="200"/>
      <h1>{album.name}</h1>
      <h6>{album.year}</h6>
      <TrackSection tracks={album.tracks} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-4"/>
    </>
  );
}

export default AlbumPage;
