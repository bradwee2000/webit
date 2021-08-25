import ArtistList from './../common/ArtistList'

const TrackInfo = ({userState, eventHandler}) => {

  const selectedTrack = userState.selectedTrack

  if (!selectedTrack) {
    return <></>
  }

  return (
    <>
      <img src={selectedTrack.imageUrl} className="thumbnail m-2" alt=""/>
      <div className="d-flex flex-column text-truncate">
        <div className="text-truncate">{selectedTrack.title}</div>
        <div>
            <small className="text-muted">
              <ArtistList artists={selectedTrack.artists} eventHandler={eventHandler} />
            </small>
        </div>
      </div>
    </>
  )
}

export default TrackInfo
