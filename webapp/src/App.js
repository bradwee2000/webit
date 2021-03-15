import { useState, useEffect } from 'react'
import { Route, Switch, Link, useHistory } from 'react-router-dom';
import Header from './components/Header';
import { AlbumApi, MusicUserApi, TrackApi } from './api/Apis';
import PlayerSection from './components/player/PlayerSection';
import { LoginButton } from './components/common/Commons';
import { FetchPlayCode } from './task/Tasks'
import AudioPlayer from './components/player/AudioPlayer'
import AlbumPage from './components/AlbumPage'
import HomePage from './components/HomePage'
import QueuePage from './components/QueuePage'
import SearchMainPage from './components/SearchMainPage'
import { HeosDevice, WebDevice } from './components/device/Devices'

const defaultUserState = {
  tracks: [],
  currentTrackIndex: 0,
  isShuffle: false,
  isLoop: false,
  isPlaying: false,
  selectedAlbum: null,
  selectedTrack: null,
  isNotLoaded: true
}

const defaultDevice = {
  id: "web",
  type: "web"
}

function App() {

  const history = useHistory()
  const [isPlaying, setIsPlaying] = useState(false)
  const [userState, setUserState] = useState(defaultUserState)
  const [selectedDevice, setSelectedDevice] = useState(defaultDevice)
  const [deviceService, setDeviceService] = useState(WebDevice)
  const deviceServices = [WebDevice, HeosDevice]

  const togglePlayTrack = (trackId, isNewPlayList=true) => {
    // If playing same track, toggle play/pause
    if (userState.selectedTrack && userState.selectedTrack.id === trackId) {
      setIsPlaying(!isPlaying)
    } else {
      // Else, play new track
      if (isNewPlayList) {
          TrackApi.play(trackId).then(setUserState)
      } else {
          MusicUserApi.selectTrack(trackId).then(setUserState)
      }

      setIsPlaying(true)
    }
  }

  const togglePlayAlbum = (albumId) => {
    // if playing same album, toggle play/pause
    if (userState.selectedAlbum && userState.selectedAlbum.id === albumId) {
      setIsPlaying(!isPlaying)
    } else {
      AlbumApi.play(albumId).then(setUserState)
      setIsPlaying(true)
    }
  }

  const playNextTrack = () => {
    MusicUserApi.next().then(setUserState)
  }

  const playPrevTrack = () => {
    MusicUserApi.prev().then(setUserState)
  }

  const shuffle = (isEnabled) => {
    MusicUserApi.shuffle(isEnabled).then(setUserState)
  }

  const loop = (isEnabled) => {
    MusicUserApi.loop(isEnabled).then(setUserState)
  }

  const eventHandler = {
    onLogin(webitUser) {
        console.log(webitUser)
        MusicUserApi.get().then(setUserState)
    },

    onSearchChange(q) {
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
      playNextTrack()
    },

    onQueueTrackClick(trackId) {
      togglePlayTrack(trackId, false)
    },

    onAlbumPlay(albumId) {
      togglePlayAlbum(albumId);
    },

    onAlbumPause(albumId) {
      togglePlayAlbum(albumId);
    },

    onAlbumClick(albumId) {
      history.push({pathname: '/album/' + albumId})
    },

    onArtistClick(artist) {
      console.log("Click Artist:" + artist)
    },

    onNextClick() {
      playNextTrack()
    },

    onPrevClick() {
      playPrevTrack()
    },

    onShuffleClick(isEnabled) {
      shuffle(isEnabled)
    },

    onLoopClick(isEnabled) {
      loop(isEnabled)
    },

    onDeviceSelected(deviceId, deviceType) {
      if (selectedDevice.id === deviceId && selectedDevice.type === deviceType) {
        return // do nothing
      } else {
        const service = findSelectedDeviceService()
        if (service) {
          setSelectedDevice({id: deviceId, type: deviceType})
        }
      }
    }
  }

  const findSelectedDeviceService = () => {
    return deviceServices.find(d => d.getType() === selectedDevice.type)
  }

  // Update play code
  useEffect((e) => {
    FetchPlayCode.run()
    const fetchPlayCodeInterval = FetchPlayCode.schedule()
    return () => clearInterval(fetchPlayCodeInterval)
  });

  // Get User info on load
  useEffect(() => {
    if (userState.isNotLoaded) {
      MusicUserApi.get().then(setUserState)
    }
  })

  // Handle device changes
  useEffect(() => {
    const service = findSelectedDeviceService()
    if (service) {
      setDeviceService(service)

    }
  }, [selectedDevice])

  // Play or pause music on device
  useEffect(() => {
    if (userState.selectedTrack) {
      if (isPlaying) {
        deviceService.play(selectedDevice.id)
      } else {
        deviceService.pause(selectedDevice.id)
      }
    }
  }, [userState.selectedTrack, isPlaying, deviceService])

  return (
    <>
    <Link to="/" className="link">Home</Link>
    <div className="h-100 overflow-auto">
      <div className="container">
        <Switch>
          <Route path='/' exact>
            <HomePage userState={userState} isPlaying={isPlaying} eventHandler={eventHandler}/>
          </Route>
          <Route path='/album/:albumId' >
            <AlbumPage userState={userState} isPlaying={isPlaying} eventHandler={eventHandler}/>
          </Route>
          <Route path='/search/:query' >
            <SearchMainPage userState={userState} isPlaying={isPlaying} eventHandler={eventHandler}/>
          </Route>
          <Route path='/queue' >
            <QueuePage userState={userState} isPlaying={isPlaying} eventHandler={eventHandler}/>
          </Route>
        </Switch>
      </div>
    </div>
    <hr className="mb-5 mt-5"/>
    <div className="fixed-bottom">
      <PlayerSection eventHandler={eventHandler} userState={userState} deviceService={deviceService} selectedDevice={selectedDevice} isPlaying={isPlaying}/>
    </div>
    </>
  );
}

export default App;
