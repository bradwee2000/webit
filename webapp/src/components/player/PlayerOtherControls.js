import { useState, useEffect } from 'react'
import { useHistory, useLocation } from 'react-router-dom';
import { ProgressBar } from './../common/Commons'
import { HeosApi } from './../../api/Apis'
import { AudioPlayer, DeviceButton, DeviceMenu, MuteButton, QueueButton } from './Player'

const webDevice = {
  id: "web",
  name: "This Web Browser",
  type: "web"
}

const queuePath = "/queue"

const PlayerOtherControls = ({userState, deviceService, selectedDevice, eventHandler}) => {

  const location = useLocation();
  const history = useHistory()
  const [volume, setVolume] = useState(1)
  const [isQueueEnabled, setIsQueueEnabled] = useState(false)
  const [isDeviceEnabled, setIsDeviceEnabled] = useState(false)
  const [devices, setDevices] = useState([webDevice])
  const [selectedDeviceId, setSelectedDeviceId] = useState(webDevice.id)
  let lastVolume = 0.1 //default volume if unmute

  const fetchDevices = () => {
    HeosApi.getPlayers().then(res => {
      const heosDevices = res.map(p => ({id: p.pid, name: p.name, type: "heos"}))
      setDevices([webDevice].concat(heosDevices))
    }).catch(e => {
      // do nothing
    })
  }

  const onVolumeChange = (newVolume) => {
    const snappedVolume = Math.round(newVolume * 10) / 10 // Snap to nearest 10s
    deviceService.setVolume(selectedDevice.id, snappedVolume)
    if (volume > 0) {
      lastVolume = volume
    }
    setVolume(snappedVolume)
  }

  const onMuteClick = (e) => {
    e.stopPropagation()
    if (volume === 0) {
      setVolume(lastVolume)
    }
  }

  const onQueueClick = (isEnabled, e) => {
    e.stopPropagation()
    if (isEnabled) {
      history.push({pathname: queuePath})
    } else {
      history.goBack()
    }
    setIsQueueEnabled(isEnabled)
  }

  const onDeviceClick = (isEnabled, e) => {
    e.stopPropagation();
    if (isEnabled) {
      fetchDevices()
    }
    setIsDeviceEnabled(isEnabled)
  }

  const onDeviceSelected = (e, deviceId, deviceType) => {
    e.stopPropagation()
    eventHandler.onDeviceSelected(deviceId, deviceType);
  }

  // Adjust volume based to device volume
  const deviceVolume = deviceService.getVolume(selectedDevice.id)
  useEffect(() => {
      setVolume(deviceVolume)
  }, [deviceVolume])

  // Toggle Queue button based on URL path
  useEffect(() => {
    const isQueuePath = location.pathname === queuePath
    if (isQueuePath !== isQueueEnabled) {
      setIsQueueEnabled(isQueuePath)
    }
  }, [location.pathname])

  return (
    <>
      <div className="col-6"></div>
      <div className="col"><QueueButton onClick={onQueueClick} isEnabled={isQueueEnabled}/></div>
      <div className="col">
        <DeviceMenu isShow={isDeviceEnabled} devices={devices} selectedDevice={selectedDevice} onClick={onDeviceSelected}/>
        <DeviceButton onClick={onDeviceClick} isEnabled={isDeviceEnabled}/>
        </div>
      <div className="col"><MuteButton volume={volume} onClick={onMuteClick}/></div>
      <div className="col-3 d-flex align-items-center">
        <ProgressBar onProgressChange={onVolumeChange} progress={volume}/>
      </div>
    </>
  );
}

export default PlayerOtherControls
