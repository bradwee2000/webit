import TrackItem from './TrackItem'

const TrackList = ({items, playingTrack, isPlaying, eventHandler}) => {

  const components = items.map((item) => (
      <TrackItem key={item.id} item={item} playingTrack={playingTrack} eventHandler={eventHandler} isPlaying={isPlaying}/>
    ));

  return (
    <div className="list-group">
      {items.length > 0 ? components : ''}
    </div>
  )
}

export default TrackList
