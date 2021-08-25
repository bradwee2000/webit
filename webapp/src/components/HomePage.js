import { useState, useEffect } from 'react'
import { SearchBar } from './common/Commons';
import { AlbumRecommendationsApi } from '../api/Apis';
import AlbumSection from './album/AlbumSection';

function HomePage({isPlaying, userState, eventHandler}) {

  const [recommendation, setRecommendation] = useState({});

  useEffect(() => {
    const page = 0;
    const size = 6;
    AlbumRecommendationsApi.get(page, size).then(setRecommendation);
  }, [])

  return (
    <>
      <SearchBar eventHandler={eventHandler} className="mt-3 mb-4"/>

      <AlbumSection title="Songs to try" albums={recommendation.albumsToTry} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-4"/>
      <AlbumSection title="Recently played" albums={recommendation.recentlyPlayed} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-4"/>
      <AlbumSection title="More of what you like" albums={recommendation.similarAlbums} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-4"/>
    </>
  );
}

export default HomePage;
