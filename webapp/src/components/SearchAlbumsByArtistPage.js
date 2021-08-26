import { useParams } from "react-router-dom";
import { useState, useEffect } from 'react'
import AlbumSection from './album/AlbumSection';
import TrackSection from './track/TrackSection';
import { SearchApi } from './../api/Apis';

function SearchAlbumsByArtistPage({isPlaying, userState, eventHandler}) {

  const { query } = useParams()
  const [albums, setAlbums] = useState([])

  useEffect(() => {
    SearchApi.searchAlbumsByArtist(query, 0, 50)
        .then(res => {
                setAlbums(res.albums);
            })
  }, [query])

  return (
    <>
      <AlbumSection albums={albums} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-5"/>
    </>
  );
}

export default SearchAlbumsByArtistPage;
