import ArtistList from './../common/ArtistList'

const TrackInfo = ({playingTrack, eventHandler}) => {

  if (playingTrack === undefined) {
    return <></>
  }

  return (
    <>
      <div className="m-2">
        <img src={playingTrack.imageUrl} className="thumbnail" alt=""/>
      </div>
      <div className="row">
        <div className="col my-auto">
            <h6 className="my-0">{playingTrack.title}</h6>
            <small className="text-muted float-start">
              <ArtistList artists={playingTrack.artist} eventHandler={eventHandler} />
            </small>
        </div>
      </div>
    </>
  )
}

export default TrackInfo
