import { useParams } from "react-router-dom";
import { useState, useEffect } from 'react'
import AlbumSection from './album/AlbumSection';
import { SearchApi } from './../api/Apis';

function SearchAlbumsPage({isPlaying, userState, eventHandler}) {

  const { query } = useParams()
  const [albums, setAlbums] = useState([])

  useEffect(() => {
    SearchApi.searchAlbums(query, 0, 40).then(res => setAlbums(res.albums))
  }, [query])

  return (
    <>
      <AlbumSection albums={albums} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-4"/>
    </>
  );
}

export default SearchAlbumsPage;
