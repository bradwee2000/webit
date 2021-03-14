import { useState } from 'react'
import { SearchBar } from './common/Commons';
import AlbumSection from './album/AlbumSection';
import TrackSection from './track/TrackSection';

function HomePage({isPlaying, userState, eventHandler}) {

  const testTracks = [
    {
      id: "vPgCxjCiRxGjvq6ynmB1dP0A-_s=",
      track: 0,
      title: "Rolling in the Deep",
      albumName: "21",
      albumId: "JapNv7dPxlHE9k6skUiIFGeZeVo=",
      imageUrl: "http://localhost:8080/images/music/WT5aAV_rmOS5bgtNDuK1g2fAoJo=.jpg",
      artist: "Adele",
    },
    {
      id: "X4aZ45QREe3TOr1hSj-qUB451XM=",
      track: 0,
      title: "God Gave Rock and Roll to You",
      albumName: "100 Rock Hits - The Sound Of My Life",
      albumId: "L4fH084Z7NqGeVBk5VQNrU_gOf8=",
      imageUrl: "http://localhost:8080/images/music/2JxFLxlA7e5w2OdWNejhDyMaoEI=.jpg",
      artist: "Argent"
    }
  ]

  const testAlbums = [
    {
      id:"1",
      name:"21",
      artists:["Adele"],
      imageUrl: "http://localhost:8080/images/music/WT5aAV_rmOS5bgtNDuK1g2fAoJo=.jpg"
    },
    {
      id:"2",
      name:"21",
      artists:["Adele"],
      imageUrl: "http://localhost:8080/images/music/2JxFLxlA7e5w2OdWNejhDyMaoEI=.jpg"
    },
    {
      id:"3",
      name:"100 Shalalamakuchi",
      artists:["Shanti Shanti", "Many Moore"],
      imageUrl: "http://localhost:8080/images/music/2JxFLxlA7e5w2OdWNejhDyMaoEI=.jpg"
    },
    {
      id:"4",
      name:"21",
      artists:["Adele"]
    },
    {
      id:"5",
      name:"21",
      artists:["Adele"]
    },
    {
      id:"6",
      name:"21",
      artists:["Adele"]
    },
    {
      id:"7",
      name:"21",
      artists:["Adele"]
    },
  ];

  const [recommendation, setRecommendation] = useState({tracks: testTracks, albums: testAlbums});

  return (
    <>
      <SearchBar eventHandler={eventHandler} className="mt-3 mb-4"/>
      <TrackSection tracks={recommendation.tracks} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-4"/>
      <AlbumSection albums={recommendation.albums} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-4"/>
    </>
  );
}

export default HomePage;
