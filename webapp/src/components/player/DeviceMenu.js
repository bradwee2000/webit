const DeviceMenu = ({isShow, devices, selectedDevice, onClick}) => {
  if (!isShow) {
    return <></>
  }

  const deviceItems = devices.map(item => {
      const className = item.id === selectedDevice.id ? "deviceItem active" : "deviceItem"
      return <div key={item.id} className={className} onClick={(e) => onClick(e, item.id, item.type)}>{item.name}</div>
    });

  return (
    <>
      <div className="position-relative">
        <div className="devicePopup position-absolute translate-middle-y">
          <div className="deviceMenu">
            <h5 className="text-center p-3">Connect to a Device</h5>
            {deviceItems}
          </div>
        </div>
      </div>
    </>
  )
}

export default DeviceMenu
