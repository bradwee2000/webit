import ArtistList from './../common/ArtistList'

const TrackInfo = ({userState, eventHandler}) => {

  const selectedTrack = userState.selectedTrack

  if (!selectedTrack) {
    return <></>
  }

  return (
    <>
      <div className="m-2">
        <img src={selectedTrack.imageUrl} className="thumbnail" alt=""/>
      </div>
      <div className="row">
        <div className="col my-auto">
            <h6 className="my-0">{selectedTrack.title}</h6>
            <small className="text-muted float-start">
              <ArtistList artists={selectedTrack.artist} eventHandler={eventHandler} />
            </small>
        </div>
      </div>
    </>
  )
}

export default TrackInfo
