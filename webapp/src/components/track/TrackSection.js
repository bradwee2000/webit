import TrackList from './TrackList'

const TrackSection = ({tracks, className='', userState, isPlaying, eventHandler}) => {

  if (tracks === undefined || tracks.length === 0) {
    return <></>;
  }

  return (
    <section className={className}>
      <div className="d-flex justify-content-between">
        <h4>Tracks</h4>
        <div className="mt-1">
          <div className="link">See All</div>
        </div>
      </div>
      <TrackList items={tracks} userState={userState} isPlaying={isPlaying} eventHandler={eventHandler} />
    </section>
  )
}

export default TrackSection
