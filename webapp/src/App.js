import { useState, useEffect } from 'react'
import Header from './components/Header';
import { AlbumApi, MusicUserApi, SearchApi, TrackApi, PlayCodeApi } from './api/Apis';
import SearchBar from './components/search/SearchBar';
import AlbumSection from './components/album/AlbumSection';
import PlayerSection from './components/player/PlayerSection';
import TrackSection from './components/track/TrackSection';
import TrackPlayList from './components/common/TrackPlayList';
import { LoginButton } from './components/common/Commons';
import SecurityContext from './security/SecurityContext'
import FetchPlayCode from './function/FetchPlayCode'
import AudioPlayer from './components/player/AudioPlayer'

function App() {
  const testTracks = [
    {
      id: "vPgCxjCiRxGjvq6ynmB1dP0A-_s=",
      track: 0,
      title: "Rolling in the Deep",
      albumName: "21",
      albumId: "JapNv7dPxlHE9k6skUiIFGeZeVo=",
      imageUrl: "http://localhost:8080/images/music/b0vwgyNq2Zm7blcjLyHa.jpg",
      artist: "Adele",
    },
    {
      id: "X4aZ45QREe3TOr1hSj-qUB451XM=",
      track: 0,
      title: "God Gave Rock and Roll to You",
      albumName: "100 Rock Hits - The Sound Of My Life",
      albumId: "L4fH084Z7NqGeVBk5VQNrU_gOf8=",
      imageUrl: "http://localhost:8080/images/music/iiZaayDjwJM478tlqzcM.jpg",
      artist: "Argent"
    }
  ]

  const testAlbums = [
    {
      id:"1",
      name:"21",
      artists:["Adele"],
      imageUrl: "http://localhost:8080/images/music/b0vwgyNq2Zm7blcjLyHa.jpg"
    },
    {
      id:"2",
      name:"21",
      artists:["Adele"],
      imageUrl: "http://localhost:8080/images/music/b0vwgyNq2Zm7blcjLyHa.jpg"
    },
    {
      id:"3",
      name:"100 Shalalamakuchi",
      artists:["Shanti Shanti", "Many Moore"],
      imageUrl: "http://localhost:8080/images/music/iiZaayDjwJM478tlqzcM.jpg"
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

  const defaultUserState = {
    tracks: [],
    currentTrackIndex: 0,
    isShuffle: false,
    isLoop: false,
    isPlaying: false,
    selectedAlbum: null,
    selectedTrack: null,
  }

  const [tracks, setTracks] = useState(testTracks);
  const [albums, setAlbums] = useState(testAlbums);
  const [isPlaying, setIsPlaying] = useState(false);
  const [userState, setUserState] = useState(defaultUserState);

  const doSearch = (query) => {
    SearchApi.search(query)
      .then(setSearchResult)
  };

  const togglePlayTrack = (trackId) => {
    let isPlayNow = !isPlaying;

    // If playing same track, toggle play/pause
    if (userState.selectedTrack && userState.selectedTrack.id === trackId) {
      isPlayNow = !isPlaying
      setIsPlaying(isPlayNow)
    } else {
      // Else, play new track
      TrackApi.play(trackId).then(setUserState)
      setIsPlaying(true);
    }
  };

  const togglePlayAlbum = (albumId) => {
    let isPlayNow = !isPlaying;

    // if playing same album
    if (userState.selectedAlbum && userState.selectedAlbum.id === albumId) {
      isPlayNow = !isPlaying;
    } else {
      AlbumApi.play(albumId).then(setUserState)
      isPlayNow = true
    }
    setIsPlaying(isPlayNow)
  };

  const setSearchResult = (result) => {
    setTracks(result.tracks)
    setAlbums(result.albums)
  };

  const eventHandler = {

    onSearchSubmit(query) {
      doSearch(query);
    },

    onSearchChange(q) {
      // setQuery(q)
    },

    onSearchClear() {
    },

    onTrackPlay(trackId) {
      togglePlayTrack(trackId);
    },

    onTrackPause(trackId) {
      togglePlayTrack(trackId);
    },

    onTrackClick(trackId) {
      togglePlayTrack(trackId);
    },

    onTrackPlayFinished() {
      setIsPlaying(false)
    },

    onAlbumPlay(albumId) {
      togglePlayAlbum(albumId);
    },

    onAlbumPause(albumId) {
      togglePlayAlbum(albumId);
    },

    onAlbumClick(albumId) {
      console.log('Click Album:' + albumId)
    },

    onArtistClick(artist) {
      console.log("Click Artist:" + artist)
    },

    onNextClick() {
      MusicUserApi.next().then(res => {
        setUserState(res)
      })
    },

    onPrevClick() {
      MusicUserApi.prev().then(res => {
        setUserState(res)
      })
    },

    onShuffleClick(isEnabled) {
      MusicUserApi.shuffle(isEnabled).then(res => {
        setUserState(res)
      })
    },

    onLoopClick(isEnabled) {
      MusicUserApi.loop(isEnabled).then(res => {
        setUserState(res)
      });
    }
  }

  // Update play code
  useEffect((e) => {
    FetchPlayCode();
    const interval = setInterval(FetchPlayCode , 3 * 60 * 1000); // every 3 mins
    return () => clearInterval(interval);
  });

  // Play or pause music
  useEffect(() => {
    if (userState.selectedTrack) {
      if (isPlaying) {
        AudioPlayer.play(TrackApi.getStreamUrl(userState.selectedTrack.id));
      } else {
        AudioPlayer.pause();
      }
    }
  }, [userState.selectedTrack, isPlaying]);

  return (
    <>
      <LoginButton/>
      <div className="h-100 overflow-auto">
        <div className="container">
          <Header/>
          <SearchBar eventHandler={eventHandler} className="mt-3 mb-4"/>
          <TrackSection tracks={tracks} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-4"/>
          <AlbumSection albums={albums} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-4"/>
        </div>
      </div>
      <hr className="mb-5 mt-5"/>
      <div className="fixed-bottom">
        <PlayerSection eventHandler={eventHandler} userState={userState} isPlaying={isPlaying}/>
      </div>
    </>
  );
}

export default App;
