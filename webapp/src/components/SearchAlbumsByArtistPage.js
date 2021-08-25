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
  }, [query])

  const seeAllTracksLink = "/search/" + query + "/artists/tracks"
  const seeAllAlbumsLink = "/search/" + query + "/artists/albums"

  const trackSection = searchType === 'all' || searchType === 'tracks' ? <TrackSection tracks={tracks} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} seeAllLink={seeAllTracksLink} className="mb-4"/> : ""
  const albumSection = searchType === 'all' || searchType === 'albums' ? <AlbumSection albums={albums} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} seeAllLink={seeAllAlbumsLink} className="mb-4"/> : ""

  return (
    <>
      {trackSection}
      {albumSection}
    </>
  );
}

export default SearchAlbumsByArtistPage;
