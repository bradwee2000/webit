import { useParams } from "react-router-dom";
import { useState, useEffect } from 'react'
import AlbumSection from './album/AlbumSection';
import TrackSection from './track/TrackSection';
import { SearchApi } from './../api/Apis';

function SearchAlbumsByArtistPage({isPlaying, userState, eventHandler}) {

  const { query, searchType } = useParams()
  const [tracks, setTracks] = useState([])
  const [albums, setAlbums] = useState([])

  useEffect(() => {

    SearchApi.searchByArtist(query, 0, 6)
        .then(res => {
                setAlbums(res.albums);
                setTracks(res.tracks);
            })
  }, [query, searchType])

  const seeAllTracksLink = "/search/" + query + "/artists/tracks"
  const seeAllAlbumsLink = "/search/" + query + "/artists/albums"

  return (
    <>
      <TrackSection tracks={tracks} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} seeAllLink={seeAllTracksLink} className="mb-5"/>
      <AlbumSection albums={albums} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} seeAllLink={seeAllAlbumsLink} className="mb-5"/>
    </>
  );
}

export default SearchAlbumsByArtistPage;
