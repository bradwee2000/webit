import TrackItem from './TrackItem'

const TrackList = ({items, userState, isPlaying, eventHandler, showAlbum=true, showTrackNum=false}) => {

  const components = items.map((item) => (
      <TrackItem key={item.id} item={item} userState={userState} showAlbum={showAlbum} showTrackNum={showTrackNum} isPlaying={isPlaying} eventHandler={eventHandler} />
    ));

  return (
    <div className="list-group">
      {items.length > 0 ? components : ''}
    </div>
  )
}

export default TrackList
