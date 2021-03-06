import { useParams } from "react-router-dom";
import { useState, useEffect } from 'react'
import AlbumSection from './album/AlbumSection'
import TrackSection from './track/TrackSection'
import { SearchApi } from './../api/Apis'

function SearchAllPage({isPlaying, userState, eventHandler}) {
  const { query } = useParams();
  const [searchResults, setSearchResults] = useState({albums:[], tracks:[]});

  const search = (query) => {
    SearchApi.search(query).then(setSearchResults)
  };

  const seeAllTracksLink = "/search/" + query + "/tracks"
  const seeAllAlbumsLink = "/search/" + query + "/albums"

  useEffect(() => {
      search(query)
  }, [query])

  return (
    <>
      <TrackSection tracks={searchResults.tracks} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} seeAllLink={seeAllTracksLink} className="mb-4"/>
      <AlbumSection albums={searchResults.albums} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} seeAllLink={seeAllAlbumsLink} className="mb-4"/>
    </>
  );
}

export default SearchAllPage;
