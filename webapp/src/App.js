import { useState, useEffect } from 'react'
import Header from './components/Header';
import { AlbumApi, SearchApi, TrackApi, PlayCodeApi } from './api/Apis';
import SearchBar from './components/search/SearchBar';
import AlbumSection from './components/album/AlbumSection';
import PlayerSection from './components/player/PlayerSection';
import TrackSection from './components/track/TrackSection';
import TrackPlayList from './components/common/TrackPlayList';
import LoginButton from './components/security/LoginButton';

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


  const [tracks, setTracks] = useState(testTracks);
  const [albums, setAlbums] = useState(testAlbums);
  const [isPlaying, setIsPlaying] = useState(false);
  const [playingTrack, setPlayingTrack] = useState();
  const [playingAlbum, setPlayingAlbum] = useState();
  const [queue, setQueue] = useState([]);

  const doSearch = (query) => {
    SearchApi.search(query, setSearchResult)
  };

  const togglePlayTrack = (trackId) => {
    let isPlayNow = !isPlaying;

    // If playing smae track
    if (playingTrack && playingTrack.id === trackId) {
      isPlayNow = !isPlaying;
    } else {
      TrackApi.get(trackId, (track) => {
        TrackPlayList.setTracks(track);
        setPlayingTrack(TrackPlayList.current());
        setPlayingAlbum(null);
      });
      isPlayNow = true;
    }
    setIsPlaying(isPlayNow);
  };

  const togglePlayAlbum = (albumId) => {
    let isPlayNow = !isPlaying;

    // if playing same album
    if (playingAlbum && playingAlbum.id === albumId) {
      isPlayNow = !isPlaying;
    } else {
      AlbumApi.get(albumId, (album) => {
        setPlayingAlbum(album);
        TrackPlayList.setTracks(album.tracks);
        setPlayingTrack(TrackPlayList.current());
      });
      isPlayNow = true;
    }
    setIsPlaying(isPlayNow);
  };

  const setSearchResult = (result) => {
    setTracks(result.tracks);
    setAlbums(result.albums);
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
      const nextTrack = TrackPlayList.next();
      if (nextTrack) {
          setPlayingTrack(nextTrack);
      }
      setIsPlaying(nextTrack != null);
    },

    onPrevClick() {
      const prevTrack = TrackPlayList.prev();
      if (prevTrack) {
          setPlayingTrack(prevTrack);
      }
      setIsPlaying(prevTrack != null)
    },

    onShuffleClick(isEnabled) {
      TrackPlayList.setShuffle(isEnabled);
    },

    onLoopClick(isEnabled) {
      TrackPlayList.setLoop(isEnabled);
    }
  }

  useEffect((e) => {
    const interval = setInterval(() => {
      PlayCodeApi.get();
    }, 3 * 60 * 1000); // every 3 mins

    return () => clearInterval(interval);
  });

  return (
    <>
      <LoginButton/>
      <div className="h-100 overflow-auto">
        <div className="container">
          <Header/>
          <SearchBar eventHandler={eventHandler} className="mt-3 mb-4"/>
          <TrackSection tracks={tracks} playingTrack={playingTrack} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-4"/>
          <AlbumSection albums={albums} playingAlbum={playingAlbum} isPlaying={isPlaying} eventHandler={eventHandler} className="mb-4"/>
        </div>
      </div>
      <div className="fixed-bottom">
        <PlayerSection eventHandler={eventHandler} playingTrack={playingTrack} isPlaying={isPlaying}/>
      </div>
    </>
  );
}

export default App;
