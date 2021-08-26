import { useParams } from "react-router-dom";
import { useState, useEffect } from 'react'
import TrackSection from './track/TrackSection';
import { SearchApi } from './../api/Apis';

function SearchTracksByArtistPage({isPlaying, userState, eventHandler}) {

  const { query } = useParams()
  const [tracks, setTracks] = useState([])

  useEffect(() => {
    SearchApi.searchTracksByArtist(query, 0, 50)
        .then(res => {
                setTracks(res.tracks);
            })
  }, [query])

  return (
    <>
      <TrackSection tracks={tracks} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-5"/>
    </>
  );
}

export default SearchTracksByArtistPage;
