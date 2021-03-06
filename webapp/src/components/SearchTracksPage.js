import { useParams } from "react-router-dom";
import { useState, useEffect } from 'react'
import TrackSection from './track/TrackSection';
import { SearchApi } from './../api/Apis';

function SearchTracksPage({isPlaying, userState, eventHandler}) {

  const { query } = useParams()
  const [tracks, setTracks] = useState([])

  useEffect(() => {
    SearchApi.searchTracks(query).then(res => setTracks(res.tracks))
  }, [query])

  return (
    <>
      <TrackSection tracks={tracks} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-4"/>
    </>
  );
}

export default SearchTracksPage;
