import PlayerControl from './PlayerControl'
import TrackInfo from './TrackInfo'

const PlayerSection = ({userState, isPlaying, eventHandler}) => {

  const otherControlComponent = (
    <>
      <div>Queue</div>
      <div>Device</div>
      <div>Volume</div>
    </>
  );

  return (
    <section className="player">
      <div className="row">
        <div className="col my-auto">
          <div className="d-flex justify-content-start">
            <TrackInfo userState={userState} eventHandler={eventHandler}/>
          </div>
        </div>
        <div className="col my-auto">
          <PlayerControl userState={userState} isPlaying={isPlaying} eventHandler={eventHandler}/>
        </div>
        <div className="col my-auto">
          <div className="d-flex justify-content-end">
            {otherControlComponent}
          </div>
        </div>
      </div>
    </section>
  )
}

export default PlayerSection
